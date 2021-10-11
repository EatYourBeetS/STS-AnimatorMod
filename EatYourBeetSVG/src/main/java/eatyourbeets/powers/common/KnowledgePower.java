package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.animator.special.Insight;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public class KnowledgePower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(KnowledgePower.class);

    public KnowledgePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        Initialize(amount);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0);
        if (amount > 0)
        {
            this.type = PowerType.BUFF;
        }
        else {
            this.type = PowerType.DEBUFF;
        }
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
    }

    @Override
    public void reducePower(int reduceAmount)
    {
        super.reducePower(reduceAmount);
    }

    @Override
    public void onRemove()
    {
        this.amount = 0;
    }

    @Override
    protected void onAmountChanged(int previousAmount, int difference)
    {
        if (difference > 0)
        {
            GameActions.Bottom.MakeCardInHand(new Insight());
            GameActions.Bottom.MakeCardInDiscardPile(new Insight());
        }

        super.onAmountChanged(previousAmount, difference);
    }
}
