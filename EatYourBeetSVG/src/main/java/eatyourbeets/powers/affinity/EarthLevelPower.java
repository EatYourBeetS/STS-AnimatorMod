package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;

public class EarthLevelPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(EarthLevelPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Earth;
    public static final String SYMBOL = "E";

    public EarthLevelPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Resistance;
    }
}