package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.Affinity;

public interface OnElementBonusSubscriber
{
    void OnElementBonus(AbstractCard card, Affinity affinity);
}
