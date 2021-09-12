package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public class TemporaryBalancePower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(TemporaryBalancePower.class);

    public TemporaryBalancePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.type = PowerType.BUFF;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        GameActions.Bottom.GainBalance(this.amount);
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        GameActions.Bottom.ReducePower(owner, owner, BalancePower.POWER_ID, this.amount);
        GameActions.Bottom.RemovePower(owner, owner, this);
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount);
    }
}