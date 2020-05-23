package eatyourbeets.actions.cardManipulation;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JavaUtilities;

public class MakeTempCard extends EYBActionWithCallback<AbstractCard>
{
    private transient AbstractGameEffect effect = null;

    protected final CardGroup cardGroup;
    protected boolean upgrade;
    protected boolean makeCopy;
    protected boolean cancelIfFull;
    protected CardSelection destination;
    protected AbstractCard actualCard;

    public MakeTempCard(AbstractCard card, CardGroup group)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_MED);

        this.card = card;
        this.cardGroup = group;

        if (!UnlockTracker.isCardSeen(card.cardID) || !card.isSeen)
        {
            UnlockTracker.markCardAsSeen(card.cardID);
            card.isLocked = false;
            card.isSeen = true;
        }

        Initialize(1);
    }

    public MakeTempCard Repeat(int times)
    {
        this.amount = times;

        if (times > 2)
        {
            SetDuration(times > 3 ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FASTER, isRealtime);
        }

        return this;
    }

    public MakeTempCard CancelIfFull(boolean cancelIfFull)
    {
        this.cancelIfFull = cancelIfFull;

        return this;
    }

    public MakeTempCard SetUpgrade(boolean upgrade, boolean makeCopy)
    {
        this.makeCopy = makeCopy;
        this.upgrade = upgrade;

        return this;
    }

    public MakeTempCard SetDestination(CardSelection destination)
    {
        this.destination = destination;

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

        if (upgrade && actualCard.canUpgrade())
        {
            actualCard.upgrade();
        }

        switch (cardGroup.type)
        {
            case DRAW_PILE:
            {
                effect = GameEffects.List.Add(new ShowCardAndAddToDrawPileEffect(actualCard,
                (float) Settings.WIDTH / 2f - ((25f * Settings.scale) + AbstractCard.IMG_WIDTH),
                (float) Settings.HEIGHT / 2f, true, true, false));

                // For reasons unknown ShowCardAndAddToDrawPileEffect creates a copy of the card...
                actualCard = JavaUtilities.<AbstractCard>GetField("card", ShowCardAndAddToDrawPileEffect.class).Get(effect);

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
                    effect = GameEffects.List.Add(new ShowCardAndAddToDiscardEffect(actualCard));
                }
                else
                {
                    // If you don't specify x and y it won't play the card obtain sfx
                    effect = GameEffects.List.Add(new ShowCardAndAddToHandEffect(actualCard,
                    (float) Settings.WIDTH / 2f - ((25f * Settings.scale) + AbstractCard.IMG_WIDTH),
                    (float) Settings.HEIGHT / 2f));
                }

                break;
            }

            case DISCARD_PILE:
            {
                effect = GameEffects.List.Add(new ShowCardAndAddToDiscardEffect(actualCard));

                break;
            }

            case EXHAUST_PILE:
            {
                effect = GameEffects.List.Add(new ExhaustCardEffect(actualCard));
                player.exhaustPile.addToTop(actualCard);
                break;
            }

            case MASTER_DECK:
            {
                GameActions.Top.Add(new AddCardToDeckAction(actualCard));

                if (destination != null)
                {
                    JavaUtilities.GetLogger(this).warn("Destination for the master deck will be ignored.");
                    destination = null;
                }

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
    protected void UpdateInternal(float deltaTime)
    {
        if (effect != null && !effect.isDone)
        {
            effect.update();
        }

        if (TickDuration(deltaTime))
        {
            if (amount > 1)
            {
                MakeTempCard copy = new MakeTempCard(actualCard, cardGroup);
                copy.Import(this);
                copy.destination = destination;
                copy.makeCopy = copy.upgrade = false;
                copy.cancelIfFull = cancelIfFull;
                copy.amount = amount - 1;
                GameActions.Top.Add(copy);
            }

            Complete(actualCard);

            if (destination != null && cardGroup.group.remove(actualCard))
            {
                destination.AddCard(cardGroup.group, actualCard, 0);
            }
        }
    }
}
