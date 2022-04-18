package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public class VitalityPower extends CommonPower
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

        if (amount > 0)
        {
            GameActions.Bottom.GainTemporaryHP(amount);
            flashWithoutSound();
        }
    }
}
