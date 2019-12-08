package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;

public class ShiroPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(ShiroPower.class.getSimpleName());

    public ShiroPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);
        this.amount = 1;

        updateDescription();
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        GameActionsHelper.GainEnergy(amount);
    }
}
