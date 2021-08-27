package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.utilities.GameActions;

public class AgilityPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(AgilityPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Green;

    public AgilityPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    protected void OnThresholdReached(int thresholdIndex)
    {
        GameActions.Top.GainDexterity(1);
    }
}