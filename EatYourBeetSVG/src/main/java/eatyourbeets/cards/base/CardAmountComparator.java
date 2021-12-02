package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.utilities.JUtils;

import java.util.Comparator;

public class CardAmountComparator implements Comparator<AbstractCard>
{
    private final boolean ascending;

    public CardAmountComparator()
    {
        this(false);
    }

    public CardAmountComparator(boolean ascending)
    {
        this.ascending = ascending;
    }

    public int compare(AbstractCard c1, AbstractCard c2)
    {
        int a = CalculateRank(c1);
        int b = CalculateRank(c2);
        return ascending ? (a - b) : (b - a);
    }

    public int CalculateRank(AbstractCard card)
    {
        EYBCard c = JUtils.SafeCast(card, EYBCard.class);
        int parsedValue = c != null ?
                (c.GetPrimaryInfo() != null ? c.GetPrimaryInfo().GetParsedValue() :
                c.GetSpecialInfo() != null ? c.GetSpecialInfo().GetParsedValue() : 0) : 0;
        int hitCount = c != null ? c.hitCount : 1;
        return card.baseDamage > 0 ? card.baseDamage * hitCount :
                card.baseBlock > 0 ? card.baseBlock : parsedValue;
    }
}