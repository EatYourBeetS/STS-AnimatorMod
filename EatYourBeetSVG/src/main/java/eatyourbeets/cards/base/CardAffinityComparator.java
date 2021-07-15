package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.utilities.JUtils;

import java.util.Comparator;

public class CardAffinityComparator implements Comparator<AbstractCard>
{
    private final AffinityType type;
    private boolean ascending;

    public CardAffinityComparator(AffinityType type)
    {
        this(type, false);
    }

    public CardAffinityComparator(AffinityType type, boolean ascending)
    {
        this.type = type;
    }

    public int compare(AbstractCard c1, AbstractCard c2)
    {
        int a = CalculateRank(c1);
        int b = CalculateRank(c2);
        return ascending ? (a - b) : (b - a);
    }

    public int CalculateRank(AbstractCard card)
    {
        AnimatorCard c = JUtils.SafeCast(card, AnimatorCard.class);
        if (c == null || c.affinities == null)
        {
            return 0;
        }

        return (c.affinities.HasStar() ? 100 : 1000) * c.affinities.GetLevel(type) + c.affinities.GetUpgrade(type);
    }
}