package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.subscribers.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class TempReduceBlockAction extends EYBActionWithCallback<AbstractCard>
        implements OnAfterCardPlayedSubscriber, OnStartOfTurnPostDrawSubscriber,
                   OnEndOfTurnSubscriber, OnAfterCardDrawnSubscriber, OnCostRefreshSubscriber
{
    protected boolean firstTimePerTurn = false;
    protected AbstractCard card;

    public TempReduceBlockAction(AbstractCard card, int amount)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FASTER);

        this.card = card;

        Initialize(amount);
    }

    @Override
    protected void FirstUpdate()
    {
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

            ReduceBlock(card);

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
            GameActions.Bottom.ModifyAllInstances(card.uuid, this::ReduceBlock);
        }

        firstTimePerTurn = false;
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer)
    {
        firstTimePerTurn = true;

        if (player.hand.contains(card))
        {
            GameActions.Bottom.ModifyAllInstances(card.uuid, this::ReduceBlock);

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
            ReduceBlock(card);
        }
    }

    @Override
    public void OnCostRefresh(AbstractCard other)
    {
        if (card.uuid.equals(other.uuid))
        {
            ReduceBlock(card);
        }
    }

    private void ReduceBlock(AbstractCard card)
    {
        GameUtilities.DecreaseBlock(card, amount, true);
    }
}
