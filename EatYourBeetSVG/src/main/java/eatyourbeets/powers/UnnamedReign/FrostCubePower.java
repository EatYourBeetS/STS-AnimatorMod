package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;

public class FrostCubePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(FrostCubePower.class.getSimpleName());

    public FrostCubePower(AbstractCreature owner, int value)
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
            GameActionsHelper.ApplyPower(owner, owner, new PlatedArmorPower(owner, amount), amount);
        }
        else for (AbstractCreature m : PlayerStatistics.GetCurrentEnemies(true))
        {
            GameActionsHelper.ApplyPower(null, m, new PlatedArmorPower(m, amount), amount);
        }
    }
}
