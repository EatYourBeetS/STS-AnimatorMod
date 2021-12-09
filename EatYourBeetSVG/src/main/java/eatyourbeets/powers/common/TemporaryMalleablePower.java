package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MalleablePower;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class TemporaryMalleablePower extends AbstractTemporaryPower
{
    public static final String POWER_ID = CreateFullID(TemporaryMalleablePower.class);

    public TemporaryMalleablePower(AbstractCreature owner, int amount)
    {
        super(owner, amount, POWER_ID, (FuncT2<AbstractPower, AbstractCreature, Integer>) MalleablePower::new);
    }

    @Override
    protected void onAmountChanged(int previousAmount, int difference)
    {
        super.onAmountChanged(previousAmount, difference);

        if (GameUtilities.GetPowerAmount(owner, MalleablePower.POWER_ID) == previousAmount && previousAmount + difference == 0) {
            GameActions.Bottom.RemovePower(owner, owner, MalleablePower.POWER_ID);
        }
    }
}
