package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.PowerHelper;

//TODO: LZLZLZ Update all these with the right powers
public class MindLevelPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(MindLevelPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Mind;
    public static final String SYMBOL = "M";

    public MindLevelPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public PowerHelper GetThresholdBonusPower()
    {
        return PowerHelper.Dexterity;
    }
}