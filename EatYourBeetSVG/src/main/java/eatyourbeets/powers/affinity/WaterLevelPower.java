package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;

public class WaterLevelPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(WaterLevelPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Water;
    public static final String SYMBOL = "W";

    public WaterLevelPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Focus;
    }
}