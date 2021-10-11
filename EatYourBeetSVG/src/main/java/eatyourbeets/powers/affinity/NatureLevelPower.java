package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;

public class NatureLevelPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(NatureLevelPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Nature;
    public static final String SYMBOL = "N";

    public NatureLevelPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Dexterity;
    }
}