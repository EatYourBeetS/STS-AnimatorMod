package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.subscribers.*;
import eatyourbeets.powers.PlayerStatistics;
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

        for (AbstractCard c : AbstractDungeon.player.hand.group)
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

        if (betterPossible.Count() > 0)
        {
            this.card = betterPossible.Retrieve(AbstractDungeon.cardRng);
        }
        else if (motivateZeroCost && possible.Count() > 0)
        {
            this.card = possible.Retrieve(AbstractDungeon.cardRng);
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

            this.card.setCostForTurn(this.card.costForTurn - amount);

            PlayerStatistics.onStartOfTurnPostDraw.Subscribe(this);
            PlayerStatistics.onEndOfTurn.Subscribe(this);
            PlayerStatistics.onAfterCardPlayed.Subscribe(this);
            PlayerStatistics.onAfterCardDrawn.Subscribe(this);
            PlayerStatistics.onCostRefresh.Subscribe(this);
        }
        else
        {
            Complete(null);
        }
    }

    @Override
    protected void UpdateInternal()
    {
        tickDuration();

        if (isDone)
        {
            Complete(card);
        }
    }

    @Override
    public void OnAfterCardPlayed(AbstractCard card)
    {
        if (this.card.uuid.equals(card.uuid))
        {
            PlayerStatistics.onStartOfTurnPostDraw.Unsubscribe(this);
            PlayerStatistics.onEndOfTurn.Unsubscribe(this);
            PlayerStatistics.onAfterCardPlayed.Unsubscribe(this);
            PlayerStatistics.onAfterCardDrawn.Unsubscribe(this);
            PlayerStatistics.onCostRefresh.Unsubscribe(this);
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (AbstractDungeon.player.hand.contains(card) && firstTimePerTurn)
        {
            GameActions.Bottom.ModifyAllCombatInstances(card.uuid, c -> c.setCostForTurn(c.costForTurn - amount));
        }

        firstTimePerTurn = false;
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer)
    {
        firstTimePerTurn = true;

        if (AbstractDungeon.player.hand.contains(card))
        {
            GameActions.Bottom.ModifyAllCombatInstances(card.uuid, c -> c.setCostForTurn(c.costForTurn - amount));

            firstTimePerTurn = false;
        }
    }

    @Override
    public void OnAfterCardDrawn(AbstractCard card)
    {
        if (firstTimePerTurn)
        {
            return;
        }

        if (this.card.uuid.equals(card.uuid))
        {
            this.card.setCostForTurn(this.card.costForTurn - amount);
        }
    }

    @Override
    public void OnCostRefresh(AbstractCard card)
    {
        if (this.card.uuid.equals(card.uuid))
        {
            this.card.setCostForTurn(this.card.costForTurn - amount);
        }
    }
}
