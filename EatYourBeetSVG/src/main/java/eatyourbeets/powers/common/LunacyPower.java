package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
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
        this.description = FormatDescription(0, amount * 2);
        if (amount > 0)
        {
            this.type = PowerType.BUFF;
        }
        else {
            this.type = PowerType.DEBUFF;
        }
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
    }

    @Override
    public void reducePower(int reduceAmount)
    {
        super.reducePower(reduceAmount);
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
            for (int i=0; i < amount*2; i++) {
                AnimatorCard randomCard = null;

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
                    GameActions.Bottom.MakeCardInDrawPile(randomCard).SetUpgrade(i % 2 == 1, false);
                }
            }
        }

        super.onAmountChanged(previousAmount, difference);
    }

    private AnimatorCard ObtainSeriesCard()
    {
        CardSeries curSeries = GR.Animator.Data.SelectedLoadout.Series;
        RandomizedList<AbstractCard> cards = GameUtilities.GetCardPoolInCombatFromRarity(null, (card) -> {
            if (!(card instanceof AnimatorCard))
            {
                return false;
            }
            return ((AnimatorCard) card).series.ID == curSeries.ID;
        });

        if (cards.Size() > 0)
        {
            return (AnimatorCard)(cards.Retrieve(rng)).makeCopy();
        }

        return null;
    }

    private AnimatorCard ObtainHindranceOrBasicCard()
    {
        CardSeries curSeries = GR.Animator.Data.SelectedLoadout.Series;
        RandomizedList<AbstractCard> cards = GameUtilities.GetCardPoolInCombatFromRarity(null, (card) -> {
            if (!(card instanceof AnimatorCard))
            {
                return false;
            }
            return (GameUtilities.IsHindrance(card)) || (card.rarity.equals(AbstractCard.CardRarity.BASIC));
        });

        if (cards.Size() > 0)
        {
            return (AnimatorCard)(cards.Retrieve(rng)).makeCopy();
        }

        return null;
    }

    private AnimatorCard ObtainRandomCard()
    {
        RandomizedList<AbstractCard> cards = GameUtilities.GetCardPoolInCombatFromRarity(null, (card) -> {
            if (!(card instanceof AnimatorCard))
            {
                return false;
            }
            return true;
        });

        if (cards.Size() > 0)
        {
            return (AnimatorCard)(cards.Retrieve(rng)).makeCopy();
        }

        return null;
    }
}
