package eatyourbeets.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

import java.lang.reflect.InvocationTargetException;

public abstract class EYBRelic extends CustomRelic
{
    protected static final FieldInfo<Float> _offsetX = JUtils.GetField("offsetX", AbstractRelic.class);

    public static AbstractPlayer player;
    public static Random rng;

    public EYBRelic(String id, String imageID, RelicTier tier, LandingSound sfx)
    {
        super(id, GR.GetTexture(GR.GetRelicImage(imageID)), tier, sfx);
    }

    public EYBRelic(String id, RelicTier tier, LandingSound sfx)
    {
        this(id, id, tier, sfx);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy()
    {
        try
        {
            return getClass().getConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            JUtils.LogError(this, e.getMessage());
            return null;
        }
    }

    protected String FormatDescription(Object... args)
    {
        return JUtils.Format(DESCRIPTIONS[0], args);
    }

    protected void DisplayAboveCreature(AbstractCreature creature)
    {
        GameActions.Top.Add(new RelicAboveCreatureAction(creature, this));
    }

    public boolean IsEnabled()
    {
        return !super.grayscale;
    }

    public boolean SetEnabled(boolean value)
    {
        super.grayscale = !value;
        return value;
    }

    public int SetCounter(int amount)
    {
        setCounter(amount);

        return counter;
    }

    public int AddCounter(int amount)
    {
        setCounter(counter + amount);

        return counter;
    }
}
