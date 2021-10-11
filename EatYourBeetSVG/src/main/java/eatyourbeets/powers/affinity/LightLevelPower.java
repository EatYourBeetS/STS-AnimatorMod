package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;

public class LightLevelPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(LightLevelPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Light;
    public static final String SYMBOL = "L";

    public LightLevelPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Supercharged;
    }
}