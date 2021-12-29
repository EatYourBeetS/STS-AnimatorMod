package pinacolada.cards.base;

import pinacolada.powers.common.BurningPower;
import pinacolada.powers.common.ElectrifiedPower;
import pinacolada.powers.common.FreezingPower;
import pinacolada.powers.replacement.PCLConstrictedPower;

public enum PCLAttackType
{
    None(false, false, null),
    Normal(false, false, null),
    Brutal(false, false, null),
    Dark(false, true, ElectrifiedPower.POWER_ID),
    Electric(false, true, PCLConstrictedPower.POWER_ID),
    Fire(false, true, FreezingPower.POWER_ID),
    Ice(false, true, BurningPower.POWER_ID),
    Piercing(true, true, null),
    Ranged(false, true, null);

    public static final int DAMAGE_MULTIPLIER = 2;
    public final boolean bypassThorns;
    public final boolean bypassBlock;
    public final String powerToRemove;

    PCLAttackType(boolean bypassBlock, boolean bypassThorns, String powerToRemove)
    {
        this.bypassThorns = bypassThorns;
        this.bypassBlock = bypassBlock;
        this.powerToRemove = powerToRemove;
    }
}
