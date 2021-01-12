package eatyourbeets.powers.deprecated;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public class LoseEnergyNextTurnPower extends CommonPower
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
            GameActions.Bottom.SpendEnergy(amount, true);
            GameActions.Bottom.RemovePower(owner, owner, this);
            flash();
        }
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount);
    }
}