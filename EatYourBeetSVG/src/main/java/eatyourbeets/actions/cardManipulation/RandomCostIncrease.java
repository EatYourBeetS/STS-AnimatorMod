package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.RandomizedList;

public class RandomCostIncrease extends EYBAction
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

        if (possible.Count() > 0)
        {
            ModifyCost(possible.Retrieve(AbstractDungeon.cardRng));
        }
    }

    private void ModifyCost(AbstractCard c)
    {
        if (permanent)
        {
            c.updateCost(Math.max(0, c.cost + amount));
        }
        else
        {
            c.setCostForTurn(Math.max(0, c.costForTurn + amount));
        }

        c.superFlash(Color.GOLD.cpy());
    }
}

