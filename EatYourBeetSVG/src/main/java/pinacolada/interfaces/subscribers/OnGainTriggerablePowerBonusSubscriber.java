package pinacolada.interfaces.subscribers;

import pinacolada.powers.PCLTriggerablePower;

public interface OnGainTriggerablePowerBonusSubscriber
{
    int OnGainTriggerablePowerBonus(String powerID, PCLTriggerablePower.Type gainType, int amount);
}
