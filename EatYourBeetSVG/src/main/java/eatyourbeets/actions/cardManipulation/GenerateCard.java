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
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.*;

public class GenerateCard extends EYBActionWithCallback<AbstractCard>
{
    private transient AbstractGameEffect effect = null;

    protected final CardGroup cardGroup;
    protected boolean upgrade;
    protected boolean makeCopy;
    protected boolean cancelIfFull;
    protected Float effectDuration;
    protected ListSelection<AbstractCard> destination;
    protected AbstractCard actualCard;
    protected SFX obtainSFX;

    public GenerateCard(AbstractCard card, CardGroup group)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FAST);

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

    public GenerateCard PlaySFX(String key, float pitchMin, float pitchMax, float volume)
    {
        obtainSFX = new SFX(key, pitchMin, pitchMax, volume);

        return this;
    }

    public GenerateCard Repeat(int times)
    {
        this.amount = times;

        if (times > 2)
        {
            SetDuration(times > 3 ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FASTER, isRealtime);
        }

        return this;
    }

    public GenerateCard CancelIfFull(boolean cancelIfFull)
    {
        this.cancelIfFull = cancelIfFull;

        return this;
    }

    public GenerateCard SetUpgrade(boolean upgrade, boolean makeCopy)
    {
        this.makeCopy = makeCopy;
        this.upgrade = upgrade;

        return this;
    }

    public GenerateCard SetDestination(ListSelection<AbstractCard> destination)
    {
        this.destination = destination;

        return this;
    }

    public GenerateCard SetEffectDuration(float duration)
    {
        this.effectDuration = duration;

        return this;
    }

    public GenerateCard ShowEffect(boolean showEffect)
    {
        if (!showEffect)
        {
            this.effectDuration = 0f;
            this.startDuration = this.duration = 0.05f;
        }

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

        if (effectDuration != null && effectDuration <= 0)
        {
            if (cardGroup.type == CardGroup.CardGroupType.HAND && player.hand.size() >= BaseMod.MAX_HAND_SIZE)
            {
                if (cancelIfFull)
                {
                    Complete();
                    return;
                }

                player.createHandIsFullDialog();
            }

            if (obtainSFX != null)
            {
                obtainSFX.Play();
            }

            CombatStats.OnCardCreated(card, false);
            cardGroup.addToTop(actualCard);
            return;
        }

        switch (cardGroup.type)
        {
            case DRAW_PILE:
            {
                effect = GameEffects.List.Add(new ShowCardAndAddToDrawPileEffect(actualCard,
                (float) Settings.WIDTH / 2f - ((25f * Settings.scale) + AbstractCard.IMG_WIDTH),
                (float) Settings.HEIGHT / 2f, true, true, false));

                // For reasons unknown ShowCardAndAddToDrawPileEffect creates a copy of the card...
                actualCard = JUtils.<AbstractCard>GetField("card", ShowCardAndAddToDrawPileEffect.class).Get(effect);

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
                    JUtils.LogWarning(this, "Destination for the master deck will be ignored.");
                    destination = null;
                }

                break;
            }

            case CARD_POOL:
            case UNSPECIFIED:
            default:
            {
                JUtils.LogWarning(this, "Can't make temp card in " + cardGroup.type.name());
                Complete();
                return;
            }
        }

        if (obtainSFX != null)
        {
            obtainSFX.Play();
        }

        if (effect != null && effectDuration != null)
        {
            effect.startingDuration = effect.duration = effectDuration;
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
                final GenerateCard copy = new GenerateCard(actualCard.makeStatEquivalentCopy(), cardGroup);
                copy.Import(this);
                copy.destination = destination;
                copy.makeCopy = copy.upgrade = false;
                copy.cancelIfFull = cancelIfFull;
                copy.effectDuration = effectDuration;
                copy.amount = amount - 1;
                GameActions.Top.Add(copy);
            }

            Complete(actualCard);

            if (destination != null && cardGroup.group.remove(actualCard))
            {
                destination.Add(cardGroup.group, actualCard, 0);
            }
        }
    }
}
