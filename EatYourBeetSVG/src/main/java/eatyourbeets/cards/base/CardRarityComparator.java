package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.Comparator;

public class CardRarityComparator implements Comparator<AbstractCard>
{
    public CardRarityComparator()
    {
    }

    public int compare(AbstractCard c1, AbstractCard c2)
    {
        return c1.rarity.compareTo(c2.rarity);
    }
}