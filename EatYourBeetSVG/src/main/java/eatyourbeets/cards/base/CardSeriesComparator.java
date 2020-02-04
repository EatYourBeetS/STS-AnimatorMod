package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.utilities.JavaUtilities;

import java.util.Comparator;

public class CardSeriesComparator implements Comparator<AbstractCard>
{
    public CardSeriesComparator()
    {
    }

    public int compare(AbstractCard c1, AbstractCard c2)
    {
        ThanksJava a1 = CalculateValue(c1);
        ThanksJava a2 = CalculateValue(c2);

        return (a1.rank - a2.rank) + (a1.synergy.Name.compareTo(a2.synergy.Name));
    }

    private ThanksJava CalculateValue(AbstractCard c1)
    {
        ThanksJava thanksJava = new ThanksJava();
        thanksJava.synergy = Synergies.ANY;

        if (c1 == null)
        {
            thanksJava.rank = 0;
            return thanksJava;
        }

        AnimatorCard card = JavaUtilities.SafeCast(c1, AnimatorCard.class);
        if (card == null)
        {
            thanksJava.rank = 1;
            return thanksJava;
        }

        Synergy synergy = card.synergy;
        if (synergy == null)
        {
            thanksJava.rank = 2;
            return thanksJava;
        }

        if (card.rarity == AbstractCard.CardRarity.SPECIAL)
        {
            thanksJava.rank = 100;
        }
        else
        {
            thanksJava.rank = 3;
        }

        thanksJava.synergy = synergy;
        return thanksJava;
    }

    private class ThanksJava
    {
        private int rank;
        private Synergy synergy;
    }
}