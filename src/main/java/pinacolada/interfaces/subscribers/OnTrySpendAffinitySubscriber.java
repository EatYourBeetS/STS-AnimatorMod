package pinacolada.interfaces.subscribers;

import pinacolada.cards.base.PCLAffinity;

public interface OnTrySpendAffinitySubscriber
{
    int OnTrySpendAffinity(PCLAffinity affinity, int amount, boolean isActuallySpending);
}