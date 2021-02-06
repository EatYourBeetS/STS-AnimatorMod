package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnCostResetSubscriber
{
    void OnCostReset(AbstractCard card);
}