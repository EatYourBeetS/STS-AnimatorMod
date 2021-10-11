package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;

public class SteelLevelPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(SteelLevelPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Steel;
    public static final String SYMBOL = "S";

    public SteelLevelPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Dexterity;
    }
}