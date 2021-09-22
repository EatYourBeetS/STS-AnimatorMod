package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;

public class BlessingPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(BlessingPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Light;
    public static final String SYMBOL = "B";

    public BlessingPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Devotion;
    }
}