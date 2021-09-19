package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;

public class CorruptionPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(CorruptionPower.class);
    public static final String TOOLTIP_ID = "~Crystallize";
    public static final Affinity AFFINITY_TYPE = Affinity.Dark;
    public static final String SYMBOL = "C";

    public CorruptionPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Temptation;
    }
}