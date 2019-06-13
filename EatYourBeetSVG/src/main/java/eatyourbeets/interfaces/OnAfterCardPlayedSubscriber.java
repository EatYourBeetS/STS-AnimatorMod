package eatyourbeets.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnAfterCardPlayedSubscriber
{
    void OnAfterCardPlayed(AbstractCard card);
}