package eatyourbeets.interfaces.subscribers;

import eatyourbeets.cards.base.Affinity;

public interface OnAffinityGainedSubscriber
{
    int OnAffinityGained(Affinity affinity, int amount);
}