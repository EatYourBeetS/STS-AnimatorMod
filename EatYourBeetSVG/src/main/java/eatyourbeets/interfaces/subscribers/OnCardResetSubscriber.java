package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnCardResetSubscriber
{
    void OnCardReset(AbstractCard card);
}