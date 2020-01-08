package eatyourbeets.actions.cardManipulation;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JavaUtilities;

public class MakeTempCard extends EYBActionWithCallback<AbstractCard>
{
    protected final CardGroup cardGroup;
    protected boolean upgrade;
    protected boolean makeCopy;
    protected boolean cancelIfFull;
    protected boolean randomSpot = true;
    protected boolean toBottom;
    protected AbstractCard actualCard;

    public MakeTempCard(AbstractCard card, CardGroup group)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_MED);

        this.card = card;
        this.cardGroup = group;
        UnlockTracker.markCardAsSeen(card.cardID);

        Initialize(1);
    }

    public MakeTempCard CancelIfFull(boolean cancelIfFull)
    {
        this.cancelIfFull = cancelIfFull;

        return this;
    }

    public MakeTempCard SetOptions(boolean upgrade, boolean makeCopy)
    {
        this.makeCopy = makeCopy;
        this.upgrade = upgrade;

        return this;
    }

    public MakeTempCard SetDestination(boolean randomSpot, boolean toBottom)
    {
        this.randomSpot = randomSpot;
        this.toBottom = toBottom;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (makeCopy)
        {
            actualCard = card.makeStatEquivalentCopy();
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
                (float) Settings.WIDTH / 2.0F - ((25.0F * Settings.scale) + AbstractCard.IMG_WIDTH),
                (float) Settings.HEIGHT / 2.0F, randomSpot, true, toBottom));

                break;
            }

            case HAND:
            {
                if (player.hand.size() >= BaseMod.MAX_HAND_SIZE)
                {
                    if (cancelIfFull)
                    {
                        Complete();
                        return;
                    }

                    player.createHandIsFullDialog();
                    GameEffects.List.Add(new ShowCardAndAddToDiscardEffect(actualCard));
                }
                else
                {
                    // If you don't specify x and y it won't play the card obtain sfx
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
                GameActions.Top.Add(new AddCardToDeckAction(actualCard));
                break;
            }

            case CARD_POOL:
            case UNSPECIFIED:
            default:
            {
                JavaUtilities.GetLogger(this).warn("Can't make temp card in " + cardGroup.type.name());
                Complete();
                break;
            }
        }
    }

    @Override
    protected void UpdateInternal()
    {
        tickDuration();

        if (isDone)
        {
            Complete(actualCard);
        }
    }
}
