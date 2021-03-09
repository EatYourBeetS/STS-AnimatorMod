package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class RandomCostIncrease extends EYBActionWithCallback<AbstractCard>
{
    public static String ID = GR.CreateID("eyb", RandomCostIncrease.class.getName());

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
            card = possible.Retrieve(rng);
        }
        else
        {
            card = null;
        }

        if (card != null)
        {
            if (permanent)
            {
                CostModifiers.For(card).Add(ID, amount);
            }
            else
            {
                GameUtilities.ModifyCostForTurn(card, amount, true);
            }

            GameUtilities.Flash(card, Color.RED, true);
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

