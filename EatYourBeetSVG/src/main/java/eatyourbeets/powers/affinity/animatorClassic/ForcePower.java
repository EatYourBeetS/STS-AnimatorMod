package eatyourbeets.powers.affinity.animatorClassic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.affinity.AnimatorClassicAffinityPower;

public class ForcePower extends AnimatorClassicAffinityPower
{
    public static final String POWER_ID = CreateFullID(ForcePower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Red;
    public static final String SYMBOL = "F";

    public ForcePower()
    {
        super(AFFINITY_TYPE, POWER_ID, SYMBOL);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Strength;
    }
}