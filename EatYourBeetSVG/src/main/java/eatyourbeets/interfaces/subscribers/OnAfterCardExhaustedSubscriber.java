package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnAfterCardExhaustedSubscriber
{
    void OnAfterCardExhausted(AbstractCard card);
}