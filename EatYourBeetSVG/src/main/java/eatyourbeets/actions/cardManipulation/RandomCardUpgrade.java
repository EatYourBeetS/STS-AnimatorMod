package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.actions.EYBActionWithCallback;
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
        RandomizedList<AbstractCard> betterPossible = new RandomizedList<>();
        RandomizedList<AbstractCard> possible = new RandomizedList<>();

        for (AbstractCard c : player.hand.group)
        {
            if (c.canUpgrade())
            {
                if (!c.upgraded)
                {
                    betterPossible.Add(c);
                }
                else
                {
                    possible.Add(c);
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
