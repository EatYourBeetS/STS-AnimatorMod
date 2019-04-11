package eatyourbeets.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnAfterCardPlayedSubscriber
{
    void OnAfterCardPlayed(AbstractCard card);
}