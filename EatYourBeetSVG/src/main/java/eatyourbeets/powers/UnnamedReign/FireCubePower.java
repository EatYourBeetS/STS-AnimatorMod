package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;

public class FireCubePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(FireCubePower.class.getSimpleName());

    public FireCubePower(AbstractCreature owner, int value)
    {
        super(owner, POWER_ID);

        this.amount = value;

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
            GameActionsHelper.ApplyPower(owner, owner, new StrengthPower(owner, amount), amount);
        }
        else for (AbstractCreature m : PlayerStatistics.GetCurrentEnemies(true))
        {
            GameActionsHelper.ApplyPower(null, m, new StrengthPower(m, amount), amount);
        }
    }
}
