package eatyourbeets.interfaces.subscribers;
import eatyourbeets.utilities.CardSelection;

public interface OnAddedToDrawPileSubscriber
{
    void OnAddedToDrawPile(boolean visualOnly, CardSelection.Mode destination);
}
