package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class EYBCardAffinityStatistics implements Iterable<EYBCardAffinityStatistics.Group>
{
    protected final ArrayList<EYBCardAffinities> cardsAffinities = new ArrayList<>();
    protected final ArrayList<Group> groups = new ArrayList<>();
    protected int cards;

    public EYBCardAffinityStatistics()
    {

    }

    public EYBCardAffinityStatistics(Collection<AbstractCard> cards)
    {
        AddCards(cards);
        RefreshStatistics(false, true);
    }

    public ArrayList<EYBCardAffinities> GetAffinities()
    {
        return cardsAffinities;
    }

    public int CardsCount()
    {
        return cards;
    }

    public void AddCard(AbstractCard card)
    {
        final EYBCardAffinities a = GetAffinitiesFromCard(card);
        if (a != null)
        {
            cardsAffinities.add(a);
        }

        cards += 1;
    }

    public void AddCards(CardGroup group)
    {
        AddCards(group.group);
    }

    public void AddCards(Collection<AbstractCard> cards)
    {
        for (AbstractCard c : cards)
        {
            AddCard(c);
        }
    }

    public void Reset()
    {
        cards = 0;
        cardsAffinities.clear();

        for (Group g : groups)
        {
            g.Reset();
        }
    }

    public ArrayList<Group> RefreshStatistics(boolean showUpgrades, boolean useStar)
    {
        for (Group g : groups)
        {
            g.Reset();
        }

        for (EYBCardAffinities a : cardsAffinities)
        {
            for (Affinity t : Affinity.All())
            {
                int level = a.GetLevel(t, useStar);
                int upgrade = a.GetUpgrade(t, useStar);
                if (showUpgrades)
                {
                    level += upgrade;
                }

                GetGroup(t).Add(level, upgrade);
            }
        }

        groups.sort(Comparator.comparingInt(a -> -a.GetTotal()));// descending
        return groups;
    }

    public Group GetGroup(int index)
    {
        return groups.get(index);
    }

    public Group GetGroup(Affinity affinity)
    {
        for (Group g : groups)
        {
            if (g.Affinity == affinity)
            {
                return g;
            }
        }

        Group g = new Group(this, affinity);
        groups.add(g);
        return g;
    }

    public static EYBCardAffinities GetAffinitiesFromCard(AbstractCard card)
    {
        return card instanceof EYBCard ? ((EYBCard) card).affinities : null;
    }

    @Override
    public Iterator<Group> iterator()
    {
        return groups.iterator();
    }

    public static class Group
    {
        public Affinity Affinity;
        public EYBCardAffinityStatistics Statistics;
        public int Size;
        public int Total_LV1;
        public int Total_LV2;
        public int Total_Upgrades;

        public Group(EYBCardAffinityStatistics statistics, Affinity affinity)
        {
            Affinity = affinity;
            Statistics = statistics;
        }

        public void Reset()
        {
            Size = Total_LV1 = Total_LV2 = Total_Upgrades = 0;
        }

        public void Add(int level, int upgrade)
        {
            Size += 1;
            Total_Upgrades += upgrade;
            if (level == 1)
            {
                Total_LV1 += 1;
            }
            else if (level > 1)
            {
                Total_LV2 += 1;
            }
        }

        public int GetTotal()
        {
            return Total_LV1 + Total_LV2;
        }

        public int GetTotal(int level)
        {
            return level == 1 ? Total_LV1 : level > 1 ? Total_LV2 : GetTotal();
        }

        public int GetTotalUpgrades()
        {
            return Total_Upgrades;
        }

        public float GetPercentage(int level)
        {
            return GetTotal(level) / (float)Size;
        }

        public float GetUpgradePercentage()
        {
            return Total_Upgrades / (float)Size;
        }

        public String GetPercentageString(int level)
        {
            return Math.round(GetPercentage(level) * 100) + "%";
        }

        public String GetUpgradePercentageString()
        {
            return Math.round(GetUpgradePercentage() * 100) + "%";
        }

        public ArrayList<AbstractCard> GetCards()
        {
            final ArrayList<AbstractCard> cards = new ArrayList<>();
            for (EYBCardAffinities a : Statistics.GetAffinities())
            {
                if (a.GetLevel(Affinity) > 0)
                {
                    cards.add(a.Card);
                }
            }

            return cards;
        }
    }
}