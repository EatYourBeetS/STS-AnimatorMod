package eatyourbeets.actions.basic;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.effects.card.RenderCardEffect;
import eatyourbeets.interfaces.OnPhaseChangedSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.*;

public class MoveCard extends EYBActionWithCallback<AbstractCard>
{
    public static float DEFAULT_CARD_X_LEFT = (float)Settings.WIDTH * 0.35F;
    public static float DEFAULT_CARD_X_RIGHT = (float)Settings.WIDTH * 0.65F;
    public static float DEFAULT_CARD_Y = (float)Settings.HEIGHT * 0.5F;

    protected CardGroup targetPile;
    protected CardGroup sourcePile;
    protected boolean showEffect;
    protected Float card_X;
    protected Float card_Y;

    public MoveCard(AbstractCard card, CardGroup targetPile)
    {
        this(card, targetPile, null);
    }

    public MoveCard(AbstractCard card, CardGroup targetPile, CardGroup sourcePile)
    {
        super(ActionType.CARD_MANIPULATION);

        this.card = card;
        this.sourcePile = sourcePile;
        this.targetPile = targetPile;

        Initialize(1);
    }

    public MoveCard SetCardPosition(float x, float y)
    {
        card_X = x;
        card_Y = y;

        return this;
    }

    public MoveCard ShowEffect(boolean showEffect, boolean isRealtime)
    {
        float duration = showEffect ? Settings.ACTION_DUR_MED : Settings.ACTION_DUR_FAST;

        if (Settings.FAST_MODE)
        {
            duration *= 0.7f;
        }

        return ShowEffect(showEffect, isRealtime, duration);
    }

    public MoveCard ShowEffect(boolean showEffect, boolean isRealtime, float duration)
    {
        SetDuration(duration, isRealtime);

        this.showEffect = showEffect;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (sourcePile == null)
        {
            if (player.hand.contains(card))
            {
                sourcePile = player.hand;
            }
            else if (player.drawPile.contains(card))
            {
                sourcePile = player.drawPile;
            }
            else if (player.discardPile.contains(card))
            {
                sourcePile = player.discardPile;
            }
            else if (player.exhaustPile.contains(card))
            {
                sourcePile = player.exhaustPile;
            }
            else
            {
                JavaUtilities.GetLogger(getClass()).warn("Could not find card source.");
                Complete();
                return;
            }
        }

        if (sourcePile == targetPile)
        {
            Complete();
            return;
        }

        if (!sourcePile.contains(card))
        {
            JavaUtilities.GetLogger(getClass()).warn("Could not find " + card.cardID + " in " + sourcePile.type.name().toLowerCase());
            Complete();
            return;
        }

        if (this.sourcePile.type == CardGroup.CardGroupType.EXHAUST_PILE)
        {
            GameActions.Bottom.Callback(card, (card, __) -> ((AbstractCard)card).unfadeOut());

            card.current_x = CardGroup.DISCARD_PILE_X;
            card.current_y = CardGroup.DISCARD_PILE_Y + Settings.scale * 30f;

            if (showEffect)
            {
                GameEffects.List.Add(new RenderCardEffect(card, duration, isRealtime));
            }
        }
        else if (this.sourcePile.type == CardGroup.CardGroupType.DRAW_PILE)
        {
            card.current_x = CardGroup.DRAW_PILE_X;
            card.current_y = CardGroup.DRAW_PILE_Y;

            if (showEffect)
            {
                GameEffects.List.Add(new RenderCardEffect(card, duration, isRealtime));
            }
        }
        else if (this.sourcePile.type == CardGroup.CardGroupType.DISCARD_PILE)
        {
            card.current_x = CardGroup.DISCARD_PILE_X;
            card.current_y = CardGroup.DISCARD_PILE_Y;

            if (showEffect)
            {
                GameEffects.List.Add(new RenderCardEffect(card, duration, isRealtime));
            }
        }

        if (showEffect && card_X == null)
        {
            if (card.current_x < Settings.WIDTH/2f)
            {
                card_X = DEFAULT_CARD_X_LEFT;
            }
            else
            {
                card_X = DEFAULT_CARD_X_RIGHT;
            }

            if (card_Y == null)
            {
                card_Y = DEFAULT_CARD_Y;
            }
        }

        switch (targetPile.type)
        {
            case DRAW_PILE:
                MoveToDrawPile();
                break;

            case HAND:
                MoveToHand();
                break;

            case DISCARD_PILE:
                MoveToDiscardPile();
                break;

            case EXHAUST_PILE:
                MoveToExhaustPile();
                break;

            case MASTER_DECK:
            case CARD_POOL:
            case UNSPECIFIED:
                MoveToPile();
                break;
        }
    }

    @Override
    protected void UpdateInternal()
    {
        this.tickDuration();

        if (showEffect)
        {
            UpdateCard();
        }

        if (this.isDone)
        {
            Complete(card);

            if (targetPile.type == CardGroup.CardGroupType.HAND || (sourcePile != null && sourcePile.type == CardGroup.CardGroupType.HAND))
            {
                GameUtilities.RefreshHandLayout();
            }
        }
    }

    protected void MoveToDiscardPile()
    {
        if (showEffect)
        {
            ShowCard();

            callbacks.add(0, new GenericCallback<>(this::MoveToDiscardPile));
        }
        else
        {
            MoveToDiscardPile(card);
        }
    }

    protected void MoveToDiscardPile(AbstractCard card)
    {
        sourcePile.moveToDiscardPile(card);

        if (sourcePile.type != CardGroup.CardGroupType.EXHAUST_PILE)
        {
            AbstractDungeon.player.onCardDrawOrDiscard();
            card.triggerOnManualDiscard();
            GameActionManager.incrementDiscard(false);
        }
    }

    protected void MoveToDrawPile()
    {
        if (showEffect)
        {
            ShowCard();

            callbacks.add(0, new GenericCallback<>(this::MoveToDrawPile));
        }
        else
        {
            MoveToDrawPile(card);
        }
    }

    protected void MoveToDrawPile(AbstractCard card)
    {
        sourcePile.moveToDeck(card, true);
    }

    protected void MoveToExhaustPile()
    {
        if (showEffect)
        {
            ShowCard();

            callbacks.add(0, new GenericCallback<>(this::MoveToExhaustPile));
        }
        else
        {
            MoveToExhaustPile(card);
        }
    }

    protected void MoveToExhaustPile(AbstractCard card)
    {
        sourcePile.moveToExhaustPile(card);
        CardCrawlGame.dungeon.checkForPactAchievement();
        card.exhaustOnUseOnce = false;
        card.freeToPlayOnce = false;
    }

    protected void MoveToHand()
    {
        if (showEffect)
        {
            ShowCard();

            callbacks.add(0, new GenericCallback<>(this::MoveToHand));
        }
        else
        {
            MoveToHand(card);
        }
    }

    protected void MoveToHand(AbstractCard card)
    {
        if (player.hand.size() >= BaseMod.MAX_HAND_SIZE)
        {
            sourcePile.moveToDiscardPile(card);
            player.createHandIsFullDialog();
        }
        else
        {
            sourcePile.moveToHand(card, sourcePile);
            card.triggerWhenDrawn();
        }
    }

    protected void MoveToPile()
    {
        if (showEffect)
        {
            ShowCard();

            callbacks.add(0, new GenericCallback<>(this::MoveToPile));
        }
        else
        {
            MoveToPile(card);
        }
    }

    protected void MoveToPile(AbstractCard card)
    {
        card.untip();
        card.unhover();
        card.unfadeOut();
        card.targetAngle = 0;
        sourcePile.removeCard(card);
        targetPile.addToTop(card);
    }

    protected void ShowCard()
    {
        if (card.drawScale < 0.3f)
        {
            card.targetDrawScale = 0.75f;
        }

        card.untip();
        card.unhover();
        card.unfadeOut();
        card.target_x = card_X;
        card.target_y = card_Y;
        card.targetAngle = 0;
        UpdateCard();
    }

    protected void UpdateCard()
    {
        if (player.hoveredCard == card)
        {
            player.releaseCard();
        }

        card.target_x = card_X;
        card.target_y = card_Y;
        card.targetAngle = 0;
        card.hoverTimer = 0.5f;
        card.update();
    }
}
