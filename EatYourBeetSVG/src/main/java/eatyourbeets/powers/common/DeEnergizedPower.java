package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public class DeEnergizedPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(DeEnergizedPower.class);

    public DeEnergizedPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.type = PowerType.DEBUFF;

        updateDescription();
    }

    public void onEnergyRecharge()
    {
        if (owner.isPlayer)
        {
            GameActions.Bottom.SpendEnergy(amount, true);
        }
        RemovePower();
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount);
    }
}