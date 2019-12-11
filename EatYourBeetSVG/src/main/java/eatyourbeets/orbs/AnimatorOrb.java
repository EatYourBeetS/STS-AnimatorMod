package eatyourbeets.orbs;

import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.resources.Resources_Animator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

public abstract class AnimatorOrb extends AbstractOrb
{
    protected static final Logger logger = LogManager.getLogger(AnimatorOrb.class.getName());

    protected final OrbStrings orbStrings;

    public static String CreateFullID(String id)
    {
        return "animator:" + id;
    }

    public AnimatorOrb(String id)
    {
        this.orbStrings = Resources_Animator.GetOrbStrings(id);
        this.ID = id;
        this.name = orbStrings.NAME;
    }

    @Override
    public AbstractOrb makeCopy()
    {
        try
        {
            return getClass().getConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
