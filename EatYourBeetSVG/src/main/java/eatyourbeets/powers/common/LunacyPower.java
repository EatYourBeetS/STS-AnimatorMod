package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class LunacyPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(LunacyPower.class);

    int maxAmount = 10;

    public LunacyPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        Initialize(amount);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, Math.min(maxAmount, (amount + 1)* 2));
        if (amount > 0)
        {
            this.type = PowerType.BUFF;
        }
        else {
            this.type = PowerType.DEBUFF;
        }
    }

    @Override
    public void onRemove()
    {
        this.amount = 0;
    }

    @Override
    protected void onAmountChanged(int previousAmount, int difference)
    {
        if (difference > 0)
        {
            int numCardsToAdd = Math.min(maxAmount, amount * 2);

            for (int i=0; i < numCardsToAdd; i++) {
                AbstractCard randomCard = null;

                if (i == 0) {
                    //Guaranteed card in same series as starting series
                    randomCard = ObtainSeriesCard();
                }
                else if (i % 4 == 0) {
                    //Guaranteed cards that is either a Hindrance or a Basic every 4 cards.
                    randomCard = ObtainHindranceOrBasicCard();
                }
                else {
                    randomCard = ObtainRandomCard();
                }

                if (randomCard != null)
                {
                    //Every 2 cards gets upgraded
                    GameActions.Bottom.MakeCardInDrawPile(randomCard).SetUpgrade(i % 2 == 1, false).SetDuration(Settings.ACTION_DUR_XFAST, true);
                }
            }
        }

        super.onAmountChanged(previousAmount, difference);
    }

    private AbstractCard ObtainSeriesCard()
    {
        CardSeries curSeries = GR.Animator.Data.SelectedLoadout.Series;
        RandomizedList<AbstractCard> cards = GameUtilities.GetRandomizedCardPool((card) -> {
            if (!(card instanceof AnimatorCard))
            {
                return false;
            }
            return ((AnimatorCard) card).series != null && ((AnimatorCard) card).series.ID == curSeries.ID;
        });

        if (cards.Size() > 0)
        {
            return (cards.Retrieve(rng)).makeCopy();
        }

        return null;
    }

    private AbstractCard ObtainHindranceOrBasicCard()
    {
        RandomizedList<AbstractCard> cards = GameUtilities.GetRandomizedCardPool( (card) -> (GameUtilities.IsHindrance(card)) || (card.rarity.equals(AbstractCard.CardRarity.BASIC)));

        if (cards.Size() > 0)
        {
            return (cards.Retrieve(rng)).makeCopy();
        }

        return null;
    }

    private AbstractCard ObtainRandomCard()
    {
        RandomizedList<AbstractCard> cards = GameUtilities.GetRandomizedCardPool( (card) -> {
            if (!(card instanceof AnimatorCard))
            {
                return false;
            }
            return true;
        });

        if (cards.Size() > 0)
        {
            return (cards.Retrieve(rng)).makeCopy();
        }

        return null;
    }
}
