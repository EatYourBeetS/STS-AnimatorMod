package eatyourbeets;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.misc.WeightedList;
import eatyourbeets.orbs.Earth;
import eatyourbeets.orbs.Fire;
import eatyourbeets.orbs.Air;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.function.Predicate;

public class Utilities
{
    public static final Logger Logger = LogManager.getLogger(Utilities.class.getName());

    private static final WeightedList<AbstractOrb> orbs = new WeightedList<>();

    public static <T> T SafeCast(Object o, Class<T> type)
    {
        return type.isInstance(o) ? type.cast(o) : null;
    }

    public static <T> T GetRandomElement(ArrayList<T> list)
    {
        return GetRandomElement(list, AbstractDungeon.miscRng);
    }

    public static <T> T GetRandomElement(ArrayList<T> list, Random rng)
    {
        int size = list.size();
        if (size > 0)
        {
            return list.get(rng.random(list.size() - 1));
        }

        return null;
    }

    public static <T> Field<T> GetPrivateField(String fieldName, Class type)
    {
        java.lang.reflect.Field field = null;
        try
        {
            field = type.getDeclaredField(fieldName);
            field.setAccessible(true);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }

        return new Field<>(field);
    }

    public static AbstractOrb GetRandomOrb()
    {
        if (orbs.Count() == 0)
        {
            orbs.Add(new Lightning(), 7);
            orbs.Add(new Frost()    , 7);
            orbs.Add(new Earth()    , 6);
            orbs.Add(new Fire()     , 6);
            orbs.Add(new Plasma()   , 4);
            orbs.Add(new Dark()     , 4);
            orbs.Add(new Air()     , 4);
        }

        return orbs.Retrieve(AbstractDungeon.miscRng, false).makeCopy();
    }

    public static <T> ArrayList<T> Where(ArrayList<T> list, Predicate<T> predicate)
    {
        ArrayList<T> res = new ArrayList<>();
        for (T t : list)
        {
            if (predicate.test(t))
            {
                res.add(t);
            }
        }

        return res;
    }
}
