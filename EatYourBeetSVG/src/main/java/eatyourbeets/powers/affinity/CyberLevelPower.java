package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;

public class CyberLevelPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(CyberLevelPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Cyber;
    public static final String SYMBOL = "C";

    public CyberLevelPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Overclock;
    }
}