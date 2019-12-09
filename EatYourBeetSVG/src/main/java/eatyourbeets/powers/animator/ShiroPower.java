package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class ShiroPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(ShiroPower.class.getSimpleName());

    public ShiroPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        GameActions.Bottom.GainEnergy(amount);
    }
}
