package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.actions.animator.EveDamageAction;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.base.AnimatorCard;

public class EvePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(EvePower.class.getSimpleName());
    public static final int GROWTH_AMOUNT = 1;

    public EvePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        super.onAfterCardPlayed(usedCard);

        AnimatorCard card = JavaUtilities.SafeCast(usedCard, AnimatorCard.class);
        if (card != null && card.HasActiveSynergy())
        {
            GameActions.Bottom.Add(new EveDamageAction(this));
        }
    }
}