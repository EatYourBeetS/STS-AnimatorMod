package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.common.BalancePower;
import eatyourbeets.utilities.GameActions;

public class WillpowerPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(WillpowerPower.class);
    public static final String SECONDARY_ID = "Balance";
    public static final Affinity AFFINITY_TYPE = Affinity.Orange;

    public WillpowerPower()
    {
        super(AFFINITY_TYPE, POWER_ID, SECONDARY_ID);
    }

    @Override
    protected void OnThresholdReached(int threshold)
    {
        GameActions.Bottom.StackPower(new BalancePower(owner, 1));
    }
}