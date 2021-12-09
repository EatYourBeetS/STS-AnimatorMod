package eatyourbeets.interfaces.subscribers;

import eatyourbeets.cards.base.Affinity;

public interface OnTrySpendAffinitySubscriber
{
    int OnTrySpendAffinity(Affinity affinity, int amount, boolean isActuallySpending);
}