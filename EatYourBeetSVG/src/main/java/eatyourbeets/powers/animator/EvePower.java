package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.actions.animator.EveDamageAction;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.AnimatorCard;

public class EvePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(EvePower.class.getSimpleName());

    public int growth;

    public EvePower(AbstractCreature owner, int initialDamage, int growth)
    {
        super(owner, POWER_ID);
        this.amount = initialDamage;
        this.growth = growth;

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        this.growth += stackAmount;
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        super.onAfterCardPlayed(usedCard);

        AnimatorCard card = JavaUtilities.SafeCast(usedCard, AnimatorCard.class);
        if (card != null && card.HasActiveSynergy())
        {
            GameActionsHelper.AddToBottom(new EveDamageAction(this));
        }
    }
}