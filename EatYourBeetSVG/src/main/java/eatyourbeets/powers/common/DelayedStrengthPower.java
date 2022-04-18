package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public class DelayedStrengthPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(DelayedStrengthPower.class);

    public DelayedStrengthPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        GameActions.Bottom.StackPower(new StrengthPower(owner, amount));
        RemovePower();
    }
}
