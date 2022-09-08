package eatyourbeets.powers.affinity.animatorClassic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.affinity.AnimatorClassicAffinityPower;

public class CorruptionPower extends AnimatorClassicAffinityPower
{
    public static final String POWER_ID = CreateFullID(CorruptionPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Dark;
    public static final String SYMBOL = "C";

    public CorruptionPower()
    {
        super(AFFINITY_TYPE, POWER_ID, SYMBOL);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Invocation;
    }
}