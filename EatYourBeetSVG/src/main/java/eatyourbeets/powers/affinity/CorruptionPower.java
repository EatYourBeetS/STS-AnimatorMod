package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;

public class CorruptionPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(CorruptionPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Dark;
    public static final String SYMBOL = "C";

    public CorruptionPower()
    {
        super(AFFINITY_TYPE, POWER_ID, SYMBOL);
    }

    @Override
    protected PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Invocation;
    }
}