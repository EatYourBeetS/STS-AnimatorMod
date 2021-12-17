package pinacolada.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import pinacolada.utilities.PCLJUtils;

import java.util.Comparator;

public class CardAffinityComparator implements Comparator<AbstractCard>
{
    private final PCLAffinity affinity;
    private boolean ascending;

    public CardAffinityComparator(PCLAffinity affinity)
    {
        this(affinity, false);
    }

    public CardAffinityComparator(PCLAffinity affinity, boolean ascending)
    {
        this.affinity = affinity;
    }

    public int compare(AbstractCard c1, AbstractCard c2)
    {
        int a = CalculateRank(c1);
        int b = CalculateRank(c2);
        return ascending ? (a - b) : (b - a);
    }

    public int CalculateRank(AbstractCard card)
    {
        PCLCard c = PCLJUtils.SafeCast(card, PCLCard.class);
        if (c == null || c.affinities == null)
        {
            return 0;
        }

        return (c.affinities.HasStar() ? 100 : 1000) * c.affinities.GetLevel(affinity) + c.affinities.GetUpgrade(affinity);
    }
}