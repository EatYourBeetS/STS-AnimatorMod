package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;

public class WillpowerPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(WillpowerPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Orange;

    public WillpowerPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Endurance;
    }
}