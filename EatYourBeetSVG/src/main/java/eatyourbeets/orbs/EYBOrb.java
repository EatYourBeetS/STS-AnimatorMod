package eatyourbeets.orbs;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

import java.lang.reflect.InvocationTargetException;

public abstract class EYBOrb extends AbstractOrb
{
    protected boolean passiveAtEndOfTurn;
    protected final OrbStrings orbStrings;

    public EYBOrb(String id, boolean passiveAtEndOfTurn)
    {
        this.orbStrings = GR.GetOrbStrings(id);
        this.ID = id;
        this.name = orbStrings.NAME;
        this.passiveAtEndOfTurn = passiveAtEndOfTurn;
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

    public void onChannel()
    {

    }

    @Override
    public void onEvoke()
    {
        Evoke();
    }

    @Override
    public void onEndOfTurn()
    {
        super.onEndOfTurn();

        if (passiveAtEndOfTurn)
        {
            Passive();
        }
    }

    @Override
    public void onStartOfTurn()
    {
        super.onStartOfTurn();

        if (!passiveAtEndOfTurn)
        {
            Passive();
        }
    }

    public void Passive()
    {
        CombatStats.OnOrbPassiveEffect(this);
    }

    public void Evoke()
    {
        // Orb Evoke event is already broadcast
    }
}
