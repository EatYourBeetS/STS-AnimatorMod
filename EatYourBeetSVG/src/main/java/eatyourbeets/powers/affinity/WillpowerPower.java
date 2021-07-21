package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.powers.common.ResiliencePower;
import eatyourbeets.utilities.GameActions;

public class WillpowerPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(WillpowerPower.class);
    public static final AffinityType AFFINITY_TYPE = AffinityType.Orange;

    public WillpowerPower(AbstractCreature owner, int amount)
    {
        super(AFFINITY_TYPE, POWER_ID, owner, amount);
    }

    @Override
    protected void OnThresholdReached(int threshold)
    {
        GameActions.Bottom.StackPower(new ResiliencePower(owner, 1));
    }
}