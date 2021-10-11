package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;

public class DarkLevelPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(DarkLevelPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Dark;
    public static final String SYMBOL = "D";

    public DarkLevelPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Lunacy;
    }
}