package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.Affinity;

public interface OnAffinityBonusSubscriber
{
    void OnAffinityBonus(AbstractCard card, Affinity affinity);
}
