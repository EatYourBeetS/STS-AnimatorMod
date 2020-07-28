package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnCardCreatedSubscriber
{
    void OnCardCreated(AbstractCard card, boolean startOfBattle);
}
