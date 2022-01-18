package pinacolada.interfaces.subscribers;

import pinacolada.powers.PCLCombatStats;

public interface OnGainPowerBonusSubscriber
{
    int OnGainPowerBonus(String powerID, PCLCombatStats.Type gainType, int amount);
}
