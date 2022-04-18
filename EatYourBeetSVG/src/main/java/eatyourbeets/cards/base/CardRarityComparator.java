package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.Comparator;

public class CardRarityComparator implements Comparator<AbstractCard>
{
    private static final AbstractCard.CardRarity[] RARITIES = AbstractCard.CardRarity.values();

    public CardRarityComparator()
    {
    }

    public int compare(AbstractCard c1, AbstractCard c2)
    {
        final int v1 = c1.rarity == AbstractCard.CardRarity.SPECIAL ? RARITIES.length : c1.rarity.ordinal();
        final int v2 = c2.rarity == AbstractCard.CardRarity.SPECIAL ? RARITIES.length : c2.rarity.ordinal();
        return v1 - v2;
        //return c1.rarity.compareTo(c2.rarity);
    }
}