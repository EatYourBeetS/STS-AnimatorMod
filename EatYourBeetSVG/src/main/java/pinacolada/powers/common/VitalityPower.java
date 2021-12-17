package pinacolada.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class VitalityPower extends PCLPower
{
    public static final String POWER_ID = CreateFullID(VitalityPower.class);

    public VitalityPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.maxAmount = 12;

        Initialize(amount);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, maxAmount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        PCLActions.Bottom.GainTemporaryHP(amount);
    }
}
