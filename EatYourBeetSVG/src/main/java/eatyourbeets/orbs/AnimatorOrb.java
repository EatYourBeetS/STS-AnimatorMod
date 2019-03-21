package eatyourbeets.orbs;

import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.AnimatorResources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AnimatorOrb extends AbstractOrb
{
    protected static final Logger logger = LogManager.getLogger(AnimatorOrb.class.getName());

    protected final OrbStrings orbStrings;

    public AnimatorOrb(String id)
    {
        orbStrings = AnimatorResources.GetOrbStrings(id);
        this.ID = id;
        this.name = orbStrings.NAME;
    }

    public static String CreateFullID(String id)
    {
        return "Animator_" + id;
    }

    @Override
    public AbstractOrb makeCopy()
    {
        try
        {
            return getClass().newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
