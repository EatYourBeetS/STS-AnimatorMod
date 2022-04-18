package eatyourbeets.interfaces.listeners;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnAddToDeckListener
{
    default boolean OnAddToDeck()
    {
        return true;
    }

    default boolean OnAddToDeck(AbstractCard card)
    {
        return OnAddToDeck();
    }
}