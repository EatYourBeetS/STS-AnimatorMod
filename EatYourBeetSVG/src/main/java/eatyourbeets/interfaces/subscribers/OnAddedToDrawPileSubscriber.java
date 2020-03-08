package eatyourbeets.interfaces.subscribers;

import eatyourbeets.actions.cardManipulation.MakeTempCard;

public interface OnAddedToDrawPileSubscriber
{
    void OnAddedToDrawPile(boolean visualOnly, MakeTempCard.Destination destination);
}
