package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.powers.common.FortuityPower;
import eatyourbeets.utilities.GameActions;

public class LuckPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(LuckPower.class);
    public static final AffinityType AFFINITY_TYPE = AffinityType.Orange;

    public LuckPower(AbstractCreature owner, int amount)
    {
        super(AFFINITY_TYPE, POWER_ID, owner, amount);
    }

    @Override
    protected void OnThresholdReached(int threshold)
    {
        GameActions.Bottom.StackPower(new FortuityPower(owner, 1));
    }
}