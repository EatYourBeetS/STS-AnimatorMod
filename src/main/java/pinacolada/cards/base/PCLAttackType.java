package pinacolada.cards.base;

import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.common.BurningPower;
import pinacolada.powers.common.ElectrifiedPower;
import pinacolada.powers.common.FreezingPower;
import pinacolada.powers.common.RippledPower;

public enum PCLAttackType
{
    None(false, false, null, 0),
    Normal(false, false, null, 0),
    Brutal(false, false, null, 0),
    Dark(false, true, ElectrifiedPower.POWER_ID, 5),
    Electric(false, true, RippledPower.POWER_ID, 5),
    Fire(false, true, FreezingPower.POWER_ID, 2),
    Ice(false, true, BurningPower.POWER_ID, 5),
    Piercing(true, true, null, 0),
    Ranged(false, true, null, 0);

    private static final int DAMAGE_MULTIPLIER = 60;
    public final boolean bypassThorns;
    public final boolean bypassBlock;
    public final String powerToRemove;
    public final int reactionIncrease;

    PCLAttackType(boolean bypassBlock, boolean bypassThorns, String powerToRemove, int reactionIncrease)
    {
        this.bypassThorns = bypassThorns;
        this.bypassBlock = bypassBlock;
        this.powerToRemove = powerToRemove;
        this.reactionIncrease = reactionIncrease;
    }

    public float GetDamageMultiplier() {
        return 1f + (GetDamageMultiplierForDisplay() / 100f);
    }

    public int GetDamageMultiplierForDisplay() {
        return DAMAGE_MULTIPLIER + PCLCombatStats.GetAmplifierBonus(powerToRemove);
    }
}
