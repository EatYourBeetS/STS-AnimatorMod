package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class BlessingPower extends PlayerAttributePower
{
    public static final String POWER_ID = CreateFullID(BlessingPower.class);

    protected static final int[] THRESHOLDS = new int[]{ 4, 8, 12, 16 };
    protected int tempHP = 1;

    public BlessingPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);
    }

    public static void PreserveOnce()
    {
        preservedPowers.Subscribe(POWER_ID);
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
        if (highestThreshold < thresholds.length)
        {
            this.description = JUtils.Format(description + powerStrings.DESCRIPTIONS[1], name, thresholds[highestThreshold], 1, tempHP);
        }
        else
        {
            this.description = JUtils.Format(description, name, highestThreshold, 1, tempHP);
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
}