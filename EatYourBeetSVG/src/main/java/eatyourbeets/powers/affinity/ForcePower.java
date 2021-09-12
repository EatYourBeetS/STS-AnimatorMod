package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;

public class ForcePower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(ForcePower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Red;
    public static final String SYMBOL = "F";

    public ForcePower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    protected PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Strength;
    }
}