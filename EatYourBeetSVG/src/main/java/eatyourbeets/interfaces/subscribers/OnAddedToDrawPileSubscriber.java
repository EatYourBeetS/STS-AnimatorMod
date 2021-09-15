package eatyourbeets.interfaces.subscribers;

import eatyourbeets.utilities.ListSelection;

public interface OnAddedToDrawPileSubscriber
{
    void OnAddedToDrawPile(boolean visualOnly, ListSelection.Mode destination);
}
