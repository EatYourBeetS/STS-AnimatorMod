package pinacolada.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.stances.NeutralStance;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class ExitStanceNextTurnPower extends PCLPower
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

        PCLActions.Bottom.ChangeStance(NeutralStance.STANCE_ID);
        PCLActions.Bottom.RemovePower(owner, owner, this);
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount);
    }
}