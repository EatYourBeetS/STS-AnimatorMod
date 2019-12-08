package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.*;
import eatyourbeets.actions.EYBActionWithCallback;

public class MakeTempCard extends EYBActionWithCallback<AbstractCard>
{
    protected CardGroup cardGroup;
    protected boolean upgrade;
    protected boolean makeCopy;
    protected boolean randomSpot;
    protected boolean toBottom;

    public MakeTempCard(AbstractCard card, CardGroup group)
    {
        super(ActionType.CARD_MANIPULATION);

        this.card = card;
        this.cardGroup = group;

        Initialize(1);
    }

    public MakeTempCard SetOptions(boolean upgrade, boolean makeCopy)
    {
        this.makeCopy = makeCopy;
        this.upgrade = upgrade;

        return this;
    }

    public MakeTempCard SetOptions2(boolean randomSpot, boolean toBottom)
    {
        this.randomSpot = randomSpot;
        this.toBottom = toBottom;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        AbstractCard actualCard;

        if (makeCopy)
        {
            actualCard = card.makeCopy();
        }
        else
        {
            actualCard = card;
        }

        if (upgrade)
        {
            actualCard.upgrade();
        }

        switch (cardGroup.type)
        {
            case DRAW_PILE:
            {
                AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(actualCard, true, false));
                break;
            }

            case HAND:
            {
                AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(actualCard));
                break;
            }

            case DISCARD_PILE:
            {
                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(actualCard));
                break;
            }

            case EXHAUST_PILE:
            {
                AbstractDungeon.effectList.add(new ExhaustCardEffect(actualCard));
                player.exhaustPile.addToTop(actualCard);
                break;
            }

            case MASTER_DECK:
            case CARD_POOL:
            case UNSPECIFIED:
                return;
        }

        Complete(actualCard);
    }
}
