package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.cards.animator.ultrarare.SummoningRitual;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class CorruptionPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(CorruptionPower.class);
    public static final AffinityType AFFINITY_TYPE = AffinityType.Dark;

    protected static final int[] THRESHOLDS = new int[]{ 5, 7, 9, 12 };

    public CorruptionPower(AbstractCreature owner, int amount)
    {
        super(AFFINITY_TYPE, POWER_ID, owner, amount);
    }

    @Override
    public int[] GetThresholds()
    {
        return THRESHOLDS;
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