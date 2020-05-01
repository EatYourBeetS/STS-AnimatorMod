package eatyourbeets.powers.deprecated;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public class DeenergizedPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(DeenergizedPower.class);

    public DeenergizedPower(AbstractCreature owner, int amount)
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
            flash();
            GameActions.Bottom.RemovePower(owner, owner, this);
        }
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount);
    }
}