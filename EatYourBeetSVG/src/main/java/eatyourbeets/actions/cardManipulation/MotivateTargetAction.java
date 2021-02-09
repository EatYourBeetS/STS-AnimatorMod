package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.base.modifiers.CostModifier;
import eatyourbeets.interfaces.subscribers.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class MotivateTargetAction extends EYBActionWithCallback<AbstractCard>
        implements OnAfterCardPlayedSubscriber, OnStartOfTurnPostDrawSubscriber,
                   OnEndOfTurnSubscriber, OnAfterCardDrawnSubscriber, OnCardResetSubscriber
{
    protected boolean firstTimePerTurn = false;
    protected AbstractCard card;

    public MotivateTargetAction(AbstractCard card)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FASTER);

        this.card = card;

        Initialize(1);
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

            ReduceCost(card);

            CombatStats.onStartOfTurnPostDraw.Subscribe(this);
            CombatStats.onEndOfTurn.Subscribe(this);
            CombatStats.onAfterCardPlayed.Subscribe(this);
            CombatStats.onAfterCardDrawn.Subscribe(this);
            CombatStats.onCardReset.Subscribe(this);
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
            CostModifier.For(card).SetModifier(-amount);

            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            CombatStats.onEndOfTurn.Unsubscribe(this);
            CombatStats.onAfterCardPlayed.Unsubscribe(this);
            CombatStats.onAfterCardDrawn.Unsubscribe(this);
            CombatStats.onCardReset.Unsubscribe(this);
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (player.hand.contains(card) && firstTimePerTurn)
        {
            GameActions.Bottom.ModifyAllInstances(card.uuid, this::ReduceCost);
        }

        firstTimePerTurn = false;
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer)
    {
        firstTimePerTurn = true;

        if (player.hand.contains(card))
        {
            GameActions.Bottom.ModifyAllInstances(card.uuid, this::ReduceCost);

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
            ReduceCost(card);
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

    private void ReduceCost(AbstractCard card)
    {
        CostModifier.For(card).SetModifier(-amount);
    }
}
