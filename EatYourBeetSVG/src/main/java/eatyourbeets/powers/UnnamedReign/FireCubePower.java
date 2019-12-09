package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameUtilities;

public class FireCubePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(FireCubePower.class.getSimpleName());

    public FireCubePower(AbstractCreature owner, int value)
    {
        super(owner, POWER_ID);

        this.amount = value;
        this.priority = -99;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + amount + desc[1];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        if (owner.isPlayer)
        {
            GameActionsHelper_Legacy.ApplyPowerSilently(owner, owner, new BurningPower(owner, owner, amount), amount);
            GameActionsHelper_Legacy.ApplyPower(owner, owner, new StrengthPower(owner, amount), amount);
        }
        else for (AbstractCreature m : GameUtilities.GetCurrentEnemies(true))
        {
            GameActionsHelper_Legacy.ApplyPowerSilently(null, m, new BurningPower(m, null, amount), amount);
            GameActionsHelper_Legacy.ApplyPower(null, m, new StrengthPower(m, amount), amount);
        }
    }
}
