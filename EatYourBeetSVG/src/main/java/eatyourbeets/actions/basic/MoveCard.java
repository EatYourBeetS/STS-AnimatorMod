package eatyourbeets.actions.basic;

import basemod.BaseMod;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.card.RenderCardEffect;
import eatyourbeets.effects.card.UnfadeOutEffect;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.*;

import java.util.List;

public class MoveCard extends EYBActionWithCallback<AbstractCard>
{
    public static final CardGroup PURGE = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    public static float DEFAULT_CARD_X_LEFT = (float) Settings.WIDTH * 0.35f;
    public static float DEFAULT_CARD_X_RIGHT = (float) Settings.WIDTH * 0.65f;
    public static float DEFAULT_CARD_Y = (float) Settings.HEIGHT * 0.5f;

    protected ListSelection<AbstractCard> destination;
    protected CardGroup targetPile;
    protected CardGroup sourcePile;
    protected boolean showEffect;
    protected Vector2 targetPosition;

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
        this.destination = null;

        Initialize(1);
    }

    public MoveCard SetDestination(ActionT3<List<AbstractCard>, AbstractCard, Integer> addCard)
    {
        this.destination = CardSelection.Special(addCard, null);

        return this;
    }

    public MoveCard SetDestination(ListSelection<AbstractCard> destination)
    {
        this.destination = destination;

        return this;
    }

    public MoveCard SetCardPosition(float x, float y)
    {
        this.targetPosition = new Vector2(x, y);

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
            sourcePile = GameUtilities.FindCardGroup(card, false);

            if (sourcePile == null)
            {
                JUtils.LogWarning(this, "Could not find card source pile.");
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
            JUtils.LogWarning(this, "Could not find " + card.cardID + " in " + sourcePile.type.name().toLowerCase());
            Complete();
            return;
        }

        if (GameUtilities.TrySetPosition(sourcePile, card) && showEffect)
        {
            GameEffects.TopLevelList.Add(new RenderCardEffect(card, duration, isRealtime));
        }

        if (showEffect && targetPosition == null)
        {
            targetPosition = new Vector2();

            if (card.current_x < Settings.WIDTH / 2f)
            {
                targetPosition.x = DEFAULT_CARD_X_LEFT;
            }
            else
            {
                targetPosition.x = DEFAULT_CARD_X_RIGHT;
            }

            targetPosition.y = DEFAULT_CARD_Y;
        }

        if (targetPile == PURGE)
        {
            Purge();
            return;
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
    protected void UpdateInternal(float deltaTime)
    {
        if (showEffect && targetPile != PURGE)
        {
            UpdateCard();
        }

        if (TickDuration(deltaTime))
        {
            Complete(card);

            if (targetPile.type == CardGroup.CardGroupType.HAND || (sourcePile != null && sourcePile.type == CardGroup.CardGroupType.HAND))
            {
                GameUtilities.RefreshHandLayout();
            }

            if (sourcePile != null && sourcePile.type == CardGroup.CardGroupType.EXHAUST_PILE)
            {
                GameEffects.Queue.Add(new UnfadeOutEffect(card));
                GameActions.Bottom.Callback(() -> GameEffects.Queue.Add(new UnfadeOutEffect(card)));
            }

            if (targetPile != player.limbo && player.limbo.contains(card))
            {
                GameActions.Bottom.Add(new UnlimboAction(card, false));
            }
        }
    }

    @Override
    protected void Complete(AbstractCard result)
    {
        super.Complete(result);

        // Change card spot based on destination
        if (destination != null && targetPile.group.remove(card))
        {
            destination.Add(targetPile.group, card, 0);
        }
    }

    protected void MoveToDiscardPile()
    {
        if (showEffect)
        {
            ShowCard();

            callbacks.add(0, GenericCallback.FromT1(this::MoveToDiscardPile));
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
            player.onCardDrawOrDiscard();
            card.triggerOnManualDiscard();
            GameActionManager.incrementDiscard(false);
        }
    }

    protected void MoveToDrawPile()
    {
        if (showEffect)
        {
            ShowCard();

            callbacks.add(0, GenericCallback.FromT1(this::MoveToDrawPile));
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

            callbacks.add(0, GenericCallback.FromT1(this::MoveToExhaustPile));
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

            callbacks.add(0, GenericCallback.FromT1(this::MoveToHand));
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
            player.createHandIsFullDialog();
            sourcePile.moveToDiscardPile(card);
        }
        else
        {
            card.triggerWhenDrawn();
            SFX.Play(SFX.CARD_OBTAIN);
            sourcePile.moveToHand(card, sourcePile);
            CombatStats.OnAfterDraw(card);
        }
    }

    protected void Purge()
    {
        PURGE.clear();
        sourcePile.removeCard(card);

        if (showEffect)
        {
            ShowCard();

            final Vector2 pos = GameUtilities.TryGetPosition(sourcePile, card);
            final AbstractGameEffect effect = GameEffects.List.Add(new PurgeCardEffect(card, pos.x, pos.y));
            if (targetPosition != null)
            {
                card.target_x = targetPosition.x;
                card.target_y = targetPosition.y;
            }

            this.startDuration = (this.duration = effect.startingDuration = effect.duration = Settings.ACTION_DUR_LONG) + 0.001f;
        }
        else
        {
            SFX.Play(SFX.CARD_BURN);
        }

        CombatStats.OnPurge(card, sourcePile);
    }

    protected void MoveToPile()
    {
        if (showEffect)
        {
            ShowCard();

            callbacks.add(0, GenericCallback.FromT1(this::MoveToPile));
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
        card.target_x = targetPosition.x;
        card.target_y = targetPosition.y;
        card.targetAngle = 0;
        UpdateCard();
    }

    protected void UpdateCard()
    {
        if (player.hoveredCard == card)
        {
            player.releaseCard();
        }

        card.target_x = targetPosition.x;
        card.target_y = targetPosition.y;
        card.targetAngle = 0;
        card.hoverTimer = 0.5f;
        card.update();
    }
}
