package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.powers.common.ResiliencePower;
import eatyourbeets.utilities.GameActions;

public class WillpowerPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(WillpowerPower.class);
    public static final AffinityType AFFINITY_TYPE = AffinityType.Orange;

    public WillpowerPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    protected void OnThresholdReached(int threshold)
    {
        GameActions.Bottom.StackPower(new ResiliencePower(owner, 1));
    }
}