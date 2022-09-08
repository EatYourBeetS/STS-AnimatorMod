package eatyourbeets.powers.affinity.animator;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.affinity.AnimatorAffinityPower;

public class BlessingPower extends AnimatorAffinityPower
{
    public static final String POWER_ID = CreateFullID(BlessingPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Light;
    public static final String SYMBOL = "B";

    public BlessingPower()
    {
        super(AFFINITY_TYPE, POWER_ID, SYMBOL);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Vitality;
    }
}