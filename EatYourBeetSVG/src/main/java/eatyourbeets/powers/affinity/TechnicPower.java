package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;

public class TechnicPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(TechnicPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Silver;
    public static final String SYMBOL = "T";

    public TechnicPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Genesis;
    }

    @Override
    public int[] GetThresholds()
    {
        return new int[]{20, 40, 60};
    }
}