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
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GenericCallback;
import eatyourbeets.utilities.JavaUtilities;

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
        this(card, targetPile, null, true);
    }

    public MoveCard(AbstractCard card, CardGroup targetPile, boolean showEffect)
    {
        this(card, targetPile, null, showEffect);
    }

    public MoveCard(AbstractCard card, CardGroup targetPile, CardGroup sourcePile)
    {
        this(card, targetPile, sourcePile, true);
    }

    public MoveCard(AbstractCard card, CardGroup targetPile, CardGroup sourcePile, boolean showEffect)
    {
        super(ActionType.CARD_MANIPULATION);

        this.card = card;
        this.sourcePile = sourcePile;
        this.targetPile = targetPile;
        this.showEffect = showEffect;

        if (showEffect)
        {
            this.duration = this.startDuration = Settings.ACTION_DUR_MED;

            if (card.current_x < Settings.WIDTH/2f)
            {
                SetCardPosition(DEFAULT_CARD_X_LEFT, DEFAULT_CARD_Y);
            }
            else
            {
                SetCardPosition(DEFAULT_CARD_X_RIGHT, DEFAULT_CARD_Y);
            }
        }

        Initialize(1);
    }

    public MoveCard SetCardPosition(float x, float y)
    {
        this.duration = this.startDuration = Settings.ACTION_DUR_MED;

        showEffect = true;
        card_X = x;
        card_Y = y;

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

        if ((sourcePile == targetPile) || !sourcePile.contains(card))
        {
            Complete();
            return;
        }

        if (this.sourcePile == player.exhaustPile)
        {
            GameActions.Bottom.Callback(card, (card, __) -> ((AbstractCard)card).unfadeOut());
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
                MoveToSpecial();
                break;
        }
    }

    @Override
    protected void UpdateInternal()
    {
        this.tickDuration();

        if (showEffect)
        {
            card.update();
        }

        if (this.isDone)
        {
            Complete(card);

            if (sourcePile != null && sourcePile.type == CardGroup.CardGroupType.HAND)
            {
                player.hand.refreshHandLayout();
                player.hand.applyPowers();
                player.hand.glowCheck();
            }
        }
    }

    protected void MoveToDiscardPile()
    {
        if (showEffect)
        {
            card.target_x = card_X;
            card.target_y = card_Y;
            card.targetAngle = 0;
            card.update();

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
            card.target_x = card_X;
            card.target_y = card_Y;
            card.targetAngle = 0;
            card.update();

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
            card.target_x = card_X;
            card.target_y = card_Y;
            card.targetAngle = 0;
            card.update();

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
        if (player.hand.size() >= BaseMod.MAX_HAND_SIZE)
        {
            sourcePile.moveToDiscardPile(card);
            player.createHandIsFullDialog();
        }
        else if (!showEffect)
        {
            sourcePile.moveToHand(card, sourcePile);
            card.triggerWhenDrawn();
        }
        else if (sourcePile.type == CardGroup.CardGroupType.DRAW_PILE)
        {
            sourcePile.removeCard(card);
            sourcePile.addToTop(card);
            AbstractDungeon.player.draw(1);
        }
        else
        {
            card.untip();
            card.unhover();
            card.lighten(true);
            sourcePile.removeCard(card);
            GameEffects.List.Add(new ShowCardAndAddToHandEffect(card));
        }

        GameActions.Bottom.Add(new RefreshHandLayout());
    }

    protected void MoveToSpecial()
    {
        card.untip();
        card.unhover();
        card.lighten(true);
        card.setAngle(0.0F);
        card.drawScale = 0.12F;
        card.targetDrawScale = 0.75F;
        card.current_x = CardGroup.DRAW_PILE_X;
        card.current_y = CardGroup.DRAW_PILE_Y;

        sourcePile.removeCard(card);
        targetPile.addToTop(card);
    }
}
