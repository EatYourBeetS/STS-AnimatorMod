package eatyourbeets.interfaces.metadata;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.powers.PlayerStatistics;

public interface MartialArtist
{
    static void ApplyScaling(AbstractCard card)
    {
        ApplyScaling(card, 1);
    }

    static void ApplyScaling(AbstractCard card, int divisor)
    {
        card.magicNumber = card.baseMagicNumber + GetScaling(divisor);
        card.isMagicNumberModified = (card.baseMagicNumber != card.magicNumber);
    }

    static int GetScaling()
    {
        return GetScaling(1);
    }

    static int GetScaling(int divisor)
    {
        return Math.floorDiv(Math.max(0, PlayerStatistics.GetDexterity()), divisor);
    }
}