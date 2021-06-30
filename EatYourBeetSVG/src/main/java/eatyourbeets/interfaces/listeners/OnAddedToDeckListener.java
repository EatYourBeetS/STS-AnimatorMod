package eatyourbeets.interfaces.listeners;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnAddedToDeckListener
{
    default void OnAddedToDeck()
    {

    }

    default void OnAddedToDeck(AbstractCard card)
    {
        OnAddedToDeck();
    }
}