package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;

public class BlessingPower extends PlayerAttributePower
{
    public static final String POWER_ID = CreateFullID(BlessingPower.class);

    protected static final int[] THRESHOLDS = new int[]{ 6, 12 };

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
        GameActions.Top.GainArtifact(1);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        if (amount > 0)
        {
            GameActions.Bottom.GainTemporaryHP(1);
        }
    }
}