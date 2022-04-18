package eatyourbeets.interfaces.subscribers;

import org.apache.commons.lang3.mutable.MutableInt;

public interface OnEnergyRechargeSubscriber
{
    void OnEnergyRecharge(MutableInt previousEnergy, MutableInt currentEnergy);
} 