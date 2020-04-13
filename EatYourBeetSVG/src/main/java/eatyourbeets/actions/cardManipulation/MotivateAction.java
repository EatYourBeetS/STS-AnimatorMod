package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.subscribers.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class MotivateAction extends EYBActionWithCallback<AbstractCard>
        implements OnAfterCardPlayedSubscriber, OnStartOfTurnPostDrawSubscriber,
                   OnEndOfTurnSubscriber, OnAfterCardDrawnSubscriber, OnCostRefreshSubscriber
{
    protected boolean motivateZeroCost = true;
    protected boolean firstTimePerTurn = false;
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

            card.setCostForTurn(card.costForTurn - amount);

            CombatStats.onStartOfTurnPostDraw.Subscribe(this);
            CombatStats.onEndOfTurn.Subscribe(this);
            CombatStats.onAfterCardPlayed.Subscribe(this);
            CombatStats.onAfterCardDrawn.Subscribe(this);
            CombatStats.onCostRefresh.Subscribe(this);
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
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            CombatStats.onEndOfTurn.Unsubscribe(this);
            CombatStats.onAfterCardPlayed.Unsubscribe(this);
            CombatStats.onAfterCardDrawn.Unsubscribe(this);
            CombatStats.onCostRefresh.Unsubscribe(this);
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (player.hand.contains(card) && firstTimePerTurn)
        {
            GameActions.Bottom.ModifyAllInstances(card.uuid, c -> c.setCostForTurn(c.costForTurn - amount));
        }

        firstTimePerTurn = false;
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer)
    {
        firstTimePerTurn = true;

        if (player.hand.contains(card))
        {
            GameActions.Bottom.ModifyAllInstances(card.uuid, c -> c.setCostForTurn(c.costForTurn - amount));

            firstTimePerTurn = false;
        }
    }

    @Override
    public void OnAfterCardDrawn(AbstractCard other)
    {
        if (firstTimePerTurn)
        {
            return;
        }

        if (card.uuid.equals(other.uuid))
        {
            card.setCostForTurn(card.costForTurn - amount);
        }
    }

    @Override
    public void OnCostRefresh(AbstractCard other)
    {
        if (card.uuid.equals(other.uuid))
        {
            card.setCostForTurn(card.costForTurn - amount);
        }
    }
}
