package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;

public class ThunderLevelPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(ThunderLevelPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Thunder;
    public static final String SYMBOL = "T";

    public ThunderLevelPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Hurricane;
    }
}