package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public class ExitStanceNextTurnPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(ExitStanceNextTurnPower.class);

    public ExitStanceNextTurnPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.type = PowerType.DEBUFF;

        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        GameActions.Bottom.ChangeStance(NeutralStance.STANCE_ID);
        GameActions.Bottom.RemovePower(owner, owner, this);
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount);
    }
}