package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class BlessingPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(BlessingPower.class);
    public static final AffinityType AFFINITY_TYPE = AffinityType.Light;

    protected static final int[] THRESHOLDS = new int[]{ 4, 8, 12, 16 };
    protected int tempHP = 1;

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
        tempHP += 1;
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0];

        int[] thresholds = GetThresholds();
        if (thresholdIndex < thresholds.length)
        {
            this.description = JUtils.Format(description + powerStrings.DESCRIPTIONS[1], name, thresholds[thresholdIndex], 1, tempHP);
        }
        else
        {
            this.description = JUtils.Format(description, name, thresholdIndex, 1, tempHP);
        }
    }

    @Override
    public void atStartOfTurn()
    {
        if (amount > 0 && tempHP > 0)
        {
            GameActions.Bottom.GainTemporaryHP(tempHP);
        }

        super.atStartOfTurn();
    }

    @Override
    public void Initialize(AbstractCreature owner)
    {
        this.tempHP = 1;
        super.Initialize(owner);
    }
}