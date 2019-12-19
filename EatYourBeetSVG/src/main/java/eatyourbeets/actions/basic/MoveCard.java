package eatyourbeets.actions.basic;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JavaUtilities;

public class MoveCard extends EYBActionWithCallback<AbstractCard>
{
    protected boolean showEffect;
    protected CardGroup targetPile;
    protected CardGroup sourcePile;

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

        Initialize(1);
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
            if (player.drawPile.contains(card))
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
        }

        if (sourcePile == null)
        {
            JavaUtilities.GetLogger(getClass()).warn("Source was null");
            Complete();
            return;
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

        if (this.targetPile == player.hand && player.hand.size() >= BaseMod.MAX_HAND_SIZE)
        {
            this.sourcePile.moveToDiscardPile(card);
            this.player.createHandIsFullDialog();
        }
        else
        {
            if (showEffect)
            {
                switch (targetPile.type)
                {
                    case HAND:
                    {
                        if (sourcePile.type == CardGroup.CardGroupType.DRAW_PILE)
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

                        break;
                    }

                    case DISCARD_PILE:
                    {
                        sourcePile.removeCard(card);
                        GameEffects.List.Add(new ShowCardAndAddToDiscardEffect(card));

                        if (sourcePile.type != CardGroup.CardGroupType.EXHAUST_PILE)
                        {
                            AbstractDungeon.player.onCardDrawOrDiscard();
                            card.triggerOnManualDiscard();
                            GameActionManager.incrementDiscard(false);
                        }

                        break;
                    }

                    case EXHAUST_PILE:
                    {
                        float targetX = (float)Settings.WIDTH * 0.35F;
                        float targetY = (float)Settings.HEIGHT * 0.5F;

                        GameEffects.List.ShowCardBriefly(card, targetX, targetY);
                        GameActions.Top.Exhaust(card, sourcePile);
                        break;
                    }

                    case DRAW_PILE:
                    default:
                    {
                        MoveCardInternal();
                        break;
                    }
                }
            }
            else
            {
                MoveCardInternal();
            }
        }
    }

    @Override
    protected void UpdateInternal()
    {
        this.tickDuration();

        if (this.isDone)
        {
            Complete(card);
        }
    }

    private void MoveCardInternal()
    {
        if (targetPile.type == CardGroup.CardGroupType.HAND)
        {
            this.sourcePile.moveToHand(card, sourcePile);

            card.triggerWhenDrawn();
        }
        else if (targetPile.type == CardGroup.CardGroupType.EXHAUST_PILE)
        {
            this.sourcePile.moveToExhaustPile(card);
        }
        else if (targetPile.type == CardGroup.CardGroupType.DISCARD_PILE)
        {
            this.sourcePile.moveToDiscardPile(card);

            if (sourcePile.type != CardGroup.CardGroupType.EXHAUST_PILE)
            {
                card.triggerOnManualDiscard();
            }
        }
        else if (targetPile.type == CardGroup.CardGroupType.DRAW_PILE)
        {
            this.sourcePile.moveToDeck(card, true);
        }
        else
        {
            card.untip();
            card.unhover();
            card.lighten(true);
            card.setAngle(0.0F);
            card.drawScale = 0.12F;
            card.targetDrawScale = 0.75F;
            card.current_x = CardGroup.DRAW_PILE_X;
            card.current_y = CardGroup.DRAW_PILE_Y;
        }

        if (sourcePile != null && sourcePile.type == CardGroup.CardGroupType.HAND)
        {
            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.applyPowers();
            AbstractDungeon.player.hand.glowCheck();
        }
    }
}
