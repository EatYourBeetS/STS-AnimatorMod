package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;

public class PoisonLevelPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(PoisonLevelPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Poison;
    public static final String SYMBOL = "P";

    public PoisonLevelPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Pestilence;
    }
}