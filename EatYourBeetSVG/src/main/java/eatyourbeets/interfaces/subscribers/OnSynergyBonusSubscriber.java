package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.Affinity;

public interface OnSynergyBonusSubscriber
{
    void OnSynergyBonus(AbstractCard card, Affinity affinity);
}
