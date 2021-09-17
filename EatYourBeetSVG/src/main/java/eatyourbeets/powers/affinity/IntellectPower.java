package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;

public class IntellectPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(IntellectPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Blue;
    public static final String SYMBOL = "I";

    public IntellectPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Focus;
    }
}