package eatyourbeets.powers.affinity.animator;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.affinity.AnimatorAffinityPower;

public class IntellectPower extends AnimatorAffinityPower
{
    public static final String POWER_ID = CreateFullID(IntellectPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Blue;
    public static final String SYMBOL = "I";

    public IntellectPower()
    {
        super(AFFINITY_TYPE, POWER_ID, SYMBOL);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Focus;
    }
}