package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;

public class HinaPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(HinaPower.class.getSimpleName());

    public HinaPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
            this.flash();
    }
}

