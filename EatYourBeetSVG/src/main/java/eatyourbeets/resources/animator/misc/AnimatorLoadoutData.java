package eatyourbeets.resources.animator.misc;

import java.util.ArrayList;
import java.util.Iterator;

public class AnimatorLoadoutData implements Iterable<CardSlot>
{
    public boolean Ready = false;
    public int Gold;
    public int HP;

    protected final ArrayList<CardSlot> list = new ArrayList<>();

    public CardSlot AddSlot(int min, int max)
    {
        CardSlot slot = new CardSlot(this, min, max);
        list.add(slot);
        return slot;
    }

    public AnimatorLoadoutData MakeCopy()
    {
        AnimatorLoadoutData copy = new AnimatorLoadoutData();
        copy.Ready = Ready;
        copy.Gold = Gold;
        copy.HP = HP;
        for (CardSlot slot : list)
        {
            copy.list.add(slot.MakeCopy(copy));
        }
        return copy;
    }

    public CardSlot Get(int index)
    {
        return list.get(index);
    }

    public int Size()
    {
        return list.size();
    }

    @Override
    public Iterator<CardSlot> iterator()
    {
        return list.iterator();
    }
}
