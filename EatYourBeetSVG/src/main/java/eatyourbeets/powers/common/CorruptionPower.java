package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.cards.animator.ultrarare.SummoningRitual;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class CorruptionPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(CorruptionPower.class);

    protected static final int[] THRESHOLDS = new int[]{ 5, 7, 9, 12 };

    public static void PreserveOnce()
    {
        preservedPowers.Subscribe(POWER_ID);
    }

    public CorruptionPower(AbstractCreature owner, int amount)
    {
        super(AffinityType.Dark, POWER_ID, owner, amount);
    }

    @Override
    public int[] GetThresholds()
    {
        return THRESHOLDS;
    }

    @Override
    public float GetScaling(EYBCard card)
    {
        return card.forceScaling * amount;
    }

    @Override
    protected void OnThresholdReached(int threshold)
    {
        if (threshold == (GetThresholds().length - 1))
        {
            GameActions.Bottom.MakeCardInHand(new SummoningRitual());
        }
        else
        {
            GameActions.Bottom.MakeCardInDrawPile(new Crystallize());
        }
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0];

        int[] thresholds = GetThresholds();
        if (thresholdIndex < thresholds.length)
        {
            final String card = (thresholdIndex == (thresholds.length - 1)) ? "#pSummoning #pRitual" : ("#y" + Crystallize.DATA.Strings.NAME);
            this.description = JUtils.Format(description + powerStrings.DESCRIPTIONS[1], name, thresholds[thresholdIndex], 1, card);
        }
        else
        {
            this.description = JUtils.Format(description, name, 0, 1, "");
        }
    }
}