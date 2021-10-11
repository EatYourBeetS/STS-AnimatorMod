package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;

public class FireLevelPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(FireLevelPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Fire;
    public static final String SYMBOL = "F";

    public FireLevelPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Strength;
    }
}