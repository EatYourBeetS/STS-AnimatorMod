package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.common.VitalityPower;
import eatyourbeets.utilities.GameActions;

public class BlessingPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(BlessingPower.class);
    public static final String SECONDARY_ID = "Vitality";
    public static final Affinity AFFINITY_TYPE = Affinity.Light;

    public BlessingPower()
    {
        super(AFFINITY_TYPE, POWER_ID, SECONDARY_ID);
    }

    @Override
    protected void OnThresholdReached(int thresholdIndex)
    {
        GameActions.Bottom.StackPower(new VitalityPower(owner, 1));
    }
}