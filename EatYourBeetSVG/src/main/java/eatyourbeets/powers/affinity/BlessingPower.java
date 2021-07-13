package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.powers.common.VitalityPower;
import eatyourbeets.utilities.GameActions;

public class BlessingPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(BlessingPower.class);
    public static final AffinityType AFFINITY_TYPE = AffinityType.Light;

    protected static final int[] THRESHOLDS = new int[]{ 4, 8, 12, 16 };

    public static void PreserveOnce()
    {
        preservedPowers.Subscribe(POWER_ID);
    }

    public BlessingPower(AbstractCreature owner, int amount)
    {
        super(AFFINITY_TYPE, POWER_ID, owner, amount);
    }

    @Override
    public int[] GetThresholds()
    {
        return THRESHOLDS;
    }

    @Override
    public float GetScaling(EYBCard card)
    {
        return 0;
    }

    @Override
    protected void OnThresholdReached(int threshold)
    {
        GameActions.Bottom.StackPower(new VitalityPower(owner, 1));
    }
}