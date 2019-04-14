package eatyourbeets.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnAfterCardDiscardedSubscriber
{
    void OnAfterCardDiscarded(AbstractCard card);
}