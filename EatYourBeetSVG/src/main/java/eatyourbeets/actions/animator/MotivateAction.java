package eatyourbeets.actions.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ReduceCostAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.common.ModifyCostForTurnAction;
import eatyourbeets.interfaces.*;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.RandomizedList;

public class MotivateAction extends AnimatorAction implements OnAfterCardPlayedSubscriber, OnStartOfTurnPostDrawSubscriber,
                                                              OnEndOfTurnSubscriber, OnAfterCardDrawnSubscriber, OnCostRefreshSubscriber
{
    private boolean firstActivation = false;
    private AbstractCard card;
    private final int costReduction;

    public MotivateAction(int costReduction)
    {
        this.costReduction = costReduction;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
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
            else if (possible.Count() > 0)
            {
                this.card = possible.Retrieve(AbstractDungeon.cardRng);
            }

            if (card != null)
            {
                card.modifyCostForTurn(-costReduction);
                card.superFlash(Color.GOLD.cpy());

                PlayerStatistics.onStartOfTurnPostDraw.Subscribe(this);
                PlayerStatistics.onEndOfTurn.Subscribe(this);
                PlayerStatistics.onAfterCardPlayed.Subscribe(this);
                PlayerStatistics.onAfterCardDrawn.Subscribe(this);
                PlayerStatistics.onCostRefresh.Subscribe(this);
            }
        }

        this.tickDuration();
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
        if (AbstractDungeon.player.hand.contains(card))
        {
            GameActionsHelper.AddToBottom(new ModifyCostForTurnAction(card.uuid, -costReduction));
        }

        firstActivation = false;
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer)
    {
        firstActivation = true;
        //GameActionsHelper.AddToBottom(new ModifyCostForTurnAction(card.uuid, -costReduction));
    }

    @Override
    public void OnAfterCardDrawn(AbstractCard card)
    {
        if (firstActivation)
        {
            return;
        }

        if (this.card.uuid.equals(card.uuid))
        {
            this.card.modifyCostForTurn(-costReduction);
        }
    }

    @Override
    public void OnCostRefresh(AbstractCard card)
    {
        if (this.card.uuid.equals(card.uuid))
        {
            this.card.modifyCostForTurn(-costReduction);
        }
    }
}
