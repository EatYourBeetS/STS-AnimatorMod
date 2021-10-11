package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;

public class AirLevelPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(AirLevelPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Air;
    public static final String SYMBOL = "A";

    public AirLevelPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Dexterity;
    }
}