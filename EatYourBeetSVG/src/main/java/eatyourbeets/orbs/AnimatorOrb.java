package eatyourbeets.orbs;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

public abstract class AnimatorOrb extends AbstractOrb
{
    protected static final Logger logger = LogManager.getLogger(AnimatorOrb.class.getName());

    protected final OrbStrings orbStrings;

    public static String CreateFullID(Class<? extends AnimatorOrb> type)
    {
        return GR.Animator.CreateID(type.getSimpleName());
    }

    public AnimatorOrb(String id)
    {
        this.orbStrings = GR.GetOrbStrings(id);
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

    public static int GetFocus()
    {
        return GameUtilities.GetPowerAmount(AbstractDungeon.player, FocusPower.POWER_ID);
    }
}
