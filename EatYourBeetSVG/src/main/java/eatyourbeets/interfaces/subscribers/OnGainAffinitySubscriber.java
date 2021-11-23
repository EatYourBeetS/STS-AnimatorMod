package eatyourbeets.interfaces.subscribers;

import eatyourbeets.cards.base.Affinity;

public interface OnGainAffinitySubscriber
{
    int OnGainAffinity(Affinity affinity, int amount, boolean isActuallyGaining);
}