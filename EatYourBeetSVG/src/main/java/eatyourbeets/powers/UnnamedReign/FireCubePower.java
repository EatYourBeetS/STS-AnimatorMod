package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActions;
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
            GameActions.Bottom.ApplyPowerSilently(null, owner, new BurningPower(null, owner, amount), amount);
            GameActions.Bottom.GainStrength(amount);
        }
        else for (AbstractCreature m : GameUtilities.GetCurrentEnemies(true))
        {
            GameActions.Bottom.ApplyPowerSilently(null, m, new BurningPower(null, m, amount), amount);
            GameActions.Bottom.StackPower(null, new StrengthPower(m, amount));
        }
    }
}
