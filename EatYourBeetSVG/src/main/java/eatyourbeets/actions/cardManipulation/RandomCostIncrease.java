package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.RandomizedList;

public class RandomCostIncrease extends EYBActionWithCallback<AbstractCard>
{
    private final boolean permanent;

    public RandomCostIncrease(int amount, boolean permanent)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FASTER);

        this.permanent = permanent;

        Initialize(amount);
    }

    @Override
    protected void FirstUpdate()
    {
        RandomizedList<AbstractCard> possible = new RandomizedList<>();
        for (AbstractCard c : player.hand.group)
        {
            if (c.costForTurn >= 0)
            {
                possible.Add(c);
            }
        }

        if (possible.Size() > 0)
        {
            card = possible.Retrieve(AbstractDungeon.cardRandomRng);
        }
        else
        {
            card = null;
        }

        if (card != null)
        {
            if (permanent)
            {
                card.updateCost(Math.max(0, card.cost + amount));
            }
            else
            {
                card.setCostForTurn(Math.max(0, card.costForTurn + amount));
            }

            card.superFlash(Color.RED.cpy());
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

