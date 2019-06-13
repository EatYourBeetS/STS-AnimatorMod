package eatyourbeets.actions.common;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.*;
import eatyourbeets.actions.animator.AnimatorAction;

import java.util.ArrayList;

public class MoveSpecificCardAction extends AnimatorAction
{
    private final AbstractPlayer player;
    private final AbstractCard card;
    private final CardGroup source;
    private final CardGroup destination;
    private final boolean showEffect;

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
            if (!source.contains(card))
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
                        case DRAW_PILE:
                        {
                            //AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(card, true, false));
                            this.source.moveToDeck(card, true);
                            this.source.removeCard(card);
                            break;
                        }

                        case HAND:
                        {
                            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card));
                            this.source.removeCard(card);
                            break;
                        }

                        case DISCARD_PILE:
                        {
                            AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(card));
                            this.source.removeCard(card);
                            break;
                        }

                        case EXHAUST_PILE:
                        {
                            AbstractDungeon.effectList.add(new ExhaustCardEffect(card));
                            MoveCard();
                            break;
                        }

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
        card.untip();
        card.unhover();
        card.lighten(true);
        card.setAngle(0.0F);
        card.drawScale = 0.12F;
        card.targetDrawScale = 0.75F;
        card.current_x = CardGroup.DRAW_PILE_X;
        card.current_y = CardGroup.DRAW_PILE_Y;
        this.source.removeCard(card);
        this.destination.addToRandomSpot(card);
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.hand.applyPowers();
        AbstractDungeon.player.hand.glowCheck();
    }
}
