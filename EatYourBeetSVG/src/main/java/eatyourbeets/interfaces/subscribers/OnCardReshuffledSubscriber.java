package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

public interface OnCardReshuffledSubscriber
{
    void OnCardReshuffled(AbstractCard card, CardGroup sourcePile);
}
