package eatyourbeets.actions.common;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.*;
import eatyourbeets.actions.animator.AnimatorAction;
import eatyourbeets.utilities.GameActionsHelper;

import java.util.ArrayList;

public class MoveSpecificCardAction extends AnimatorAction
{
    private final AbstractPlayer player;
    private final AbstractCard card;
    private final CardGroup destination;
    private final boolean showEffect;

    private CardGroup source;

    public MoveSpecificCardAction(AbstractCard card, CardGroup destination)
    {
        this(card, destination, null, false);
    }

    public MoveSpecificCardAction(AbstractCard card, CardGroup destination, boolean showEffect)
    {
        this(card, destination, null, showEffect);
    }

    public MoveSpecificCardAction(AbstractCard card, CardGroup destination, CardGroup source)
    {
        this(card, destination, source, false);
    }

    public MoveSpecificCardAction(AbstractCard card, CardGroup destination, CardGroup source, boolean showEffect)
    {
        this.card = card;
        this.player = AbstractDungeon.player;
        this.source = source;
        this.destination = destination;
        this.showEffect = showEffect;

        this.setValues(this.player, AbstractDungeon.player);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update()
    {
        ArrayList callbackList;
        if (this.duration == Settings.ACTION_DUR_MED)
        {
            if (source == null)
            {
                if (player.hand.contains(card))
                {
                    source = player.hand;
                }
                if (player.drawPile.contains(card))
                {
                    source = player.drawPile;
                }
                else if (player.discardPile.contains(card))
                {
                    source = player.discardPile;
                }
                else if (player.exhaustPile.contains(card))
                {
                    source = player.exhaustPile;
                }
            }

            if (source == null)
            {
                logger.warn("Source was null, at common.MoveSpecificCardAction");
                this.isDone = true;
                return;
            }

            if ((source == destination) || !source.contains(card))
            {
                this.isDone = true;
                return;
            }

            if (this.source == player.exhaustPile)
            {
                card.unfadeOut();
            }

            if (this.destination == player.hand && player.hand.size() >= BaseMod.MAX_HAND_SIZE)
            {
                this.source.moveToDiscardPile(card);
                this.player.createHandIsFullDialog();
            }
            else
            {
                if (showEffect)
                {
                    switch (destination.type)
                    {
                        case HAND:
                        {
                            card.untip();
                            card.unhover();
                            card.lighten(true);

                            this.source.removeCard(card);
                            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card));
                            break;
                        }

                        case DISCARD_PILE:
                        {
                            this.source.removeCard(card);
                            AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(card));

                            if (source.type != CardGroup.CardGroupType.EXHAUST_PILE)
                            {
                                AbstractDungeon.player.onCardDrawOrDiscard();
                                card.triggerOnManualDiscard();
                                GameActionManager.incrementDiscard(false);
                            }

                            break;
                        }

                        case EXHAUST_PILE:
                        {
                            //AbstractDungeon.effectList.add(new ExhaustCardEffect(card));
                            GameActionsHelper.AddToTop(new ExhaustSpecificCardAction(card, source));
                            break;
                        }

                        case DRAW_PILE:
                        default:
                        {
                            MoveCard();
                            break;
                        }
                    }
                }
                else
                {
                    MoveCard();
                }
            }

            card.initializeDescription();
        }

        this.tickDuration();
    }

    private void MoveCard()
    {
        if (destination.type == CardGroup.CardGroupType.HAND)
        {
            this.source.moveToHand(card, source);

            card.triggerWhenDrawn();
        }
        else if (destination.type == CardGroup.CardGroupType.EXHAUST_PILE)
        {
            this.source.moveToExhaustPile(card);
        }
        else if (destination.type == CardGroup.CardGroupType.DISCARD_PILE)
        {
            this.source.moveToDiscardPile(card);

            if (source.type != CardGroup.CardGroupType.EXHAUST_PILE)
            {
                card.triggerOnManualDiscard();
            }
        }
        else if (destination.type == CardGroup.CardGroupType.DRAW_PILE)
        {
            this.source.moveToDeck(card, true);
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

            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.applyPowers();
            AbstractDungeon.player.hand.glowCheck();
        }
    }
}
