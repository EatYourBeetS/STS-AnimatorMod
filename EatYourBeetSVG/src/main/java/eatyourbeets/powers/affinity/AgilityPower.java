package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.utilities.GameActions;

public class AgilityPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(AgilityPower.class);
    public static final AffinityType AFFINITY_TYPE = AffinityType.Green;

    public AgilityPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    protected void OnThresholdReached(int threshold)
    {
        GameActions.Top.GainDexterity(1);
    }
}