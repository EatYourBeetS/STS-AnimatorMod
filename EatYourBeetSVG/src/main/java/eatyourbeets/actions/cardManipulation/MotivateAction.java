package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.subscribers.OnAfterCardPlayedSubscriber;
import eatyourbeets.interfaces.subscribers.OnCardResetSubscriber;
import eatyourbeets.interfaces.subscribers.OnCostResetSubscriber;
import eatyourbeets.interfaces.subscribers.OnEndOfTurnSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class MotivateAction extends EYBActionWithCallback<AbstractCard> implements OnEndOfTurnSubscriber, OnAfterCardPlayedSubscriber, OnCardResetSubscriber, OnCostResetSubscriber
{
    protected boolean motivateZeroCost = true;
    protected boolean costReduced = false;
    protected AbstractCard card;

    public MotivateAction(int amount)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FASTER);

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

        if (card != null)
        {
            if (card.costForTurn > 0)
            {
                card.superFlash(Color.GOLD.cpy());
            }
            else
            {
                Complete(card);
            }

            ReduceCost(card);

            CombatStats.onAfterCardPlayed.Subscribe(this);
            CombatStats.onCardReset.Subscribe(this);
            CombatStats.onCostReset.Subscribe(this);
        }
        else
        {
            Complete(null);
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

    @Override
    public void OnAfterCardPlayed(AbstractCard other)
    {
        if (card.uuid.equals(other.uuid))
        {
            CombatStats.onAfterCardPlayed.Unsubscribe(this);
            CombatStats.onCardReset.Unsubscribe(this);
            CombatStats.onCostReset.Unsubscribe(this);
        }
    }

    @Override
    public void OnCardReset(AbstractCard other)
    {
        if (card.uuid.equals(other.uuid))
        {
            ReduceCost(card);
        }
    }

    @Override
    public void OnCostReset(AbstractCard other)
    {
        if (card.uuid.equals(other.uuid) && !costReduced)
        {
            ReduceCost(card);
        }
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer)
    {
        costReduced = false;
    }

    private void ReduceCost(AbstractCard card)
    {
        GameUtilities.ModifyCostForTurn(card, -amount, true);
        costReduced = true;
    }
}
