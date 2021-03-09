package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class RandomCostReduction extends EYBActionWithCallback<AbstractCard>
{
    public static String ID = GR.CreateID("eyb", RandomCostReduction.class.getName());

    private final boolean permanent;

    public RandomCostReduction(int amount, boolean permanent)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FASTER);

        this.permanent = permanent;

        Initialize(amount);
    }

    @Override
    protected void FirstUpdate()
    {
        RandomizedList<AbstractCard> betterPossible = new RandomizedList<>();
        RandomizedList<AbstractCard> possible = new RandomizedList<>();

        for (AbstractCard c : player.hand.group)
        {
            if (c.costForTurn > 0)
            {
                betterPossible.Add(c);
            }
            else if (c.cost > 0)
            {
                possible.Add(c);
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
            if (permanent)
            {
                CostModifiers.For(card).Add(ID, -amount);
            }
            else
            {
                GameUtilities.ModifyCostForTurn(card, -amount, true);
            }

            GameUtilities.Flash(card, Color.GOLD, true);
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

