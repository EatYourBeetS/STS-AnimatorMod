package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class MotivateAction extends EYBActionWithCallback<AbstractCard>
{
    public static String ID = GR.CreateID("eyb", MotivateAction.class.getName());

    protected boolean motivateZeroCost = true;
    protected boolean costReduced = false;
    protected AbstractCard card;

    public MotivateAction(int amount)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FASTER);

        Initialize(amount);
    }

    public MotivateAction(AbstractCard card, int amount)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FASTER);

        this.card = card;

        Initialize(amount);
    }

    public MotivateAction MotivateZeroCost(boolean value)
    {
        this.motivateZeroCost = value;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (card == null)
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
            else if (motivateZeroCost && possible.Size() > 0)
            {
                card = possible.Retrieve(rng);
            }
        }

        if (card == null || (card.costForTurn <= 0 && !motivateZeroCost))
        {
            Complete(null);
            return;
        }

        card.superFlash(Color.GOLD.cpy());

        CostModifiers.For(card).Add(ID, -amount);
        GameUtilities.TriggerWhenPlayed(card, c -> CostModifiers.For(c).Remove(ID, false));

        if (card.costForTurn <= 0)
        {
            Complete(card);
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete(card);
        }
    }
}