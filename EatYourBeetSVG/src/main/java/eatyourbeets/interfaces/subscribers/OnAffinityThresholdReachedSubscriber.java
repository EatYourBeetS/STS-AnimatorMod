package eatyourbeets.interfaces.subscribers;

import eatyourbeets.powers.affinity.AbstractAffinityPower;

public interface OnAffinityThresholdReachedSubscriber
{
    void OnAffinityThresholdReached(AbstractAffinityPower power, int thresholdLevel);
}