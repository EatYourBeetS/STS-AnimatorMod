package pinacolada.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class DeenergizedPower extends PCLPower
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
            PCLActions.Bottom.SpendEnergy(amount, true);
            flash();
            PCLActions.Bottom.RemovePower(owner, owner, this);
        }
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount);
    }
}