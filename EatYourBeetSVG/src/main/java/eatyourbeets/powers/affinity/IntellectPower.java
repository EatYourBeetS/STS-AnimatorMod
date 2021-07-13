package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.utilities.GameActions;

public class IntellectPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(IntellectPower.class);
    public static final AffinityType AFFINITY_TYPE = AffinityType.Blue;

    public IntellectPower(AbstractCreature owner, int amount)
    {
        super(AFFINITY_TYPE, POWER_ID, owner, amount);
    }

    @Override
    protected void OnThresholdReached(int threshold)
    {
        GameActions.Top.GainFocus(1);
    }
}