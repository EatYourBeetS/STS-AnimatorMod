package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnAfterlifeSubscriber
{
    void OnAfterlife(AbstractCard playedCard, AbstractCard fuelCard);
}
