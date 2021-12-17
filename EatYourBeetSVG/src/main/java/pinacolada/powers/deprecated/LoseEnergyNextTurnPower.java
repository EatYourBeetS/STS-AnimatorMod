package pinacolada.powers.deprecated;

import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class LoseEnergyNextTurnPower extends PCLPower
{
    public static final String POWER_ID = CreateFullID(LoseEnergyNextTurnPower.class);

    public LoseEnergyNextTurnPower(AbstractCreature owner, int amount)
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
            PCLActions.Bottom.RemovePower(owner, owner, this);
            flash();
        }
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount);
    }
}