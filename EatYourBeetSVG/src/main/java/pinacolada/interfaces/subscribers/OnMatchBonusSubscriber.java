package pinacolada.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import pinacolada.cards.base.PCLAffinity;

public interface OnMatchBonusSubscriber
{
    void OnMatchBonus(AbstractCard card, PCLAffinity affinity);
}