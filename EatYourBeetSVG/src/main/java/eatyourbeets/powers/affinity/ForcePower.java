package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.utilities.GameActions;

public class ForcePower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(ForcePower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Red;

    public ForcePower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    protected void OnThresholdReached(int thresholdIndex)
    {
        GameActions.Top.GainStrength(1);
    }
}