package eatyourbeets.interfaces.subscribers;

import eatyourbeets.powers.CommonTriggerablePower;

public interface OnGainTriggerablePowerBonusSubscriber
{
    int OnGainTriggerablePowerBonus(String powerID, CommonTriggerablePower.Type gainType, int amount);
}
