package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.utilities.GameActions;

public class IntellectPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(IntellectPower.class);
    public static final String SECONDARY_ID = "~Focus";
    public static final Affinity AFFINITY_TYPE = Affinity.Blue;

    public IntellectPower()
    {
        super(AFFINITY_TYPE, POWER_ID, SECONDARY_ID);
    }

    @Override
    protected void OnThresholdReached(int thresholdIndex)
    {
        GameActions.Top.GainFocus(1);
    }
}