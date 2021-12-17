package pinacolada.interfaces.subscribers;

import pinacolada.cards.base.PCLAffinity;

public interface OnGainAffinitySubscriber
{
    int OnGainAffinity(PCLAffinity affinity, int amount, boolean isActuallyGaining);
}