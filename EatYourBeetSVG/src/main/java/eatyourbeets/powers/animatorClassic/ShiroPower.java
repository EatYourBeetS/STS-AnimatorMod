package eatyourbeets.powers.animatorClassic;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorClassicPower;
import eatyourbeets.utilities.GameActions;

public class ShiroPower extends AnimatorClassicPower
{
    public static final String POWER_ID = CreateFullID(ShiroPower.class);

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
