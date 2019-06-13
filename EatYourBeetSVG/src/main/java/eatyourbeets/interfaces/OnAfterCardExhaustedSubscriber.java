package eatyourbeets.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnAfterCardExhaustedSubscriber
{
    void OnAfterCardExhausted(AbstractCard card);
}