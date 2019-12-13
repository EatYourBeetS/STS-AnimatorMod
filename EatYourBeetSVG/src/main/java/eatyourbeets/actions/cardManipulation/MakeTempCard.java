package eatyourbeets.actions.cardManipulation;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.cardManip.*;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class MakeTempCard extends EYBActionWithCallback<AbstractCard>
{
    protected CardGroup cardGroup;
    protected boolean upgrade;
    protected boolean makeCopy;
    protected boolean randomSpot;
    protected boolean toBottom;
    protected float cardX = -1;
    protected float cardY = -1;

    public MakeTempCard(AbstractCard card, CardGroup group)
    {
        this(null, card, group);
    }

    public MakeTempCard(AbstractCard sourceCard, AbstractCard card, CardGroup group)
    {
        super(ActionType.CARD_MANIPULATION);

        if (sourceCard != null)
        {
            this.cardX = sourceCard.current_x;
            this.cardY = sourceCard.current_y;
        }

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
                GameEffects.List.Add(new ShowCardAndAddToDrawPileEffect(actualCard,
                (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F, true, false));

                break;
            }

            case HAND:
            {
                if (player.hand.size() >= BaseMod.MAX_HAND_SIZE)
                {
                    player.createHandIsFullDialog();
                    GameEffects.List.Add(new ShowCardAndAddToDiscardEffect(actualCard));
                }
                else
                {
                    GameEffects.List.Add(new ShowCardAndAddToHandEffect(actualCard,
                            (float) Settings.WIDTH / 2.0F - ((25.0F * Settings.scale) + AbstractCard.IMG_WIDTH),
                            (float) Settings.HEIGHT / 2.0F));
                }

                break;
            }

            case DISCARD_PILE:
            {
                GameEffects.List.Add(new ShowCardAndAddToDiscardEffect(actualCard));

                break;
            }

            case EXHAUST_PILE:
            {
                GameEffects.List.Add(new ExhaustCardEffect(actualCard));
                player.exhaustPile.addToTop(actualCard);
                break;
            }

            case MASTER_DECK:
            {
                GameActions.Bottom.Add(new AddCardToDeckAction(actualCard));
                break;
            }

            case CARD_POOL:
            case UNSPECIFIED:
                return;
        }

        Complete(actualCard);
    }
}
