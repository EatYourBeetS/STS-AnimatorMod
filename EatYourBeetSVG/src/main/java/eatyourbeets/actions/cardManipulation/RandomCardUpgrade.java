package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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
        RandomizedList<AbstractCard> betterPossible = new RandomizedList<>();
        RandomizedList<AbstractCard> possible = new RandomizedList<>();

        for (AbstractCard c : AbstractDungeon.player.hand.group)
        {
            if (c.canUpgrade() && !GameUtilities.IsCurseOrStatus(c))
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
            card = betterPossible.Retrieve(AbstractDungeon.cardRandomRng);
        }
        else if (possible.Size() > 0)
        {
            card = possible.Retrieve(AbstractDungeon.cardRandomRng);
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
    protected void UpdateInternal()
    {
        tickDuration();

        if (isDone && card != null)
        {
            Complete(card);
        }
    }
}
