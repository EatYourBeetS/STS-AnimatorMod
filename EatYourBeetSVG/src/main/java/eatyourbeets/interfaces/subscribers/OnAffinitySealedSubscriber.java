package eatyourbeets.interfaces.subscribers;

import eatyourbeets.cards.base.EYBCard;

public interface OnAffinitySealedSubscriber
{
    void OnAffinitySealed(EYBCard card, boolean manual);
}