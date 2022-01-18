package pinacolada.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import pinacolada.utilities.PCLJUtils;

import java.util.Comparator;

public class CardSeriesComparator implements Comparator<AbstractCard>
{
    public CardSeriesComparator()
    {
    }

    public int compare(AbstractCard c1, AbstractCard c2)
    {
        final ThanksJava a1 = CalculateValue(c1);
        final ThanksJava a2 = CalculateValue(c2);
        return (a1.rank - a2.rank) + (a1.series.LocalizedName.compareTo(a2.series.LocalizedName));
    }

    private ThanksJava CalculateValue(AbstractCard c1)
    {
        ThanksJava thanksJava = new ThanksJava();
        thanksJava.series = CardSeries.ANY;

        if (c1 == null)
        {
            thanksJava.rank = 0;
            return thanksJava;
        }

        PCLCard card = PCLJUtils.SafeCast(c1, PCLCard.class);
        if (card == null)
        {
            thanksJava.rank = 1;
            return thanksJava;
        }

        CardSeries series = card.series;
        if (series == null)
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

        thanksJava.series = series;
        return thanksJava;
    }

    private static class ThanksJava
    {
        private int rank;
        private CardSeries series;
    }
}