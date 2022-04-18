package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class RandomCardUpgrade extends EYBActionWithCallback<AbstractCard>
{
    public RandomCardUpgrade()
    {
        super(ActionType.CARD_MANIPULATION);
    }

    @Override
    protected void FirstUpdate()
    {
        final RandomizedList<AbstractCard> possible = new RandomizedList<>();
        final RandomizedList<AbstractCard> betterPossible = new RandomizedList<>();

        for (AbstractCard c : player.hand.group)
        {
            if (c.canUpgrade() && !GameUtilities.IsHindrance(c))
            {
                if (c.upgraded)
                {
                    possible.Add(c);
                }
                else
                {
                    betterPossible.Add(c);
                }
            }
        }

        if (betterPossible.Size() > 0)
        {
            card = betterPossible.Retrieve(rng);
        }
        else if (possible.Size() > 0)
        {
            card = possible.Retrieve(rng);
        }
        else
        {
            card = null;
        }

        if (card != null)
        {
            card.upgrade();
            card.flash();
        }
        else
        {
            Complete();
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime) && card != null)
        {
            Complete(card);
        }
    }
}
