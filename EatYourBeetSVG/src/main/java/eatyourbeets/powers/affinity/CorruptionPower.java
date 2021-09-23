package eatyourbeets.powers.affinity;

import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.cards.animator.ultrarare.SummoningRitual;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class CorruptionPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(CorruptionPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Dark;
    public static final String SYMBOL = "C";

    protected static final int[] THRESHOLDS = new int[]{ 3, 6, 9, 12, 15 };

    public CorruptionPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public int[] GetThresholds()
    {
        return THRESHOLDS;
    }

    @Override
    protected void RefreshThresholdBonus(boolean thresholdReached, int addModifier)
    {
        if (!thresholdReached)
        {
            return;
        }

        if (thresholdIndex >= GetThresholds().length)
        {
            GameActions.Bottom.MakeCardInHand(new SummoningRitual());
            AnimatorCard_UltraRare.MarkAsSeen(SummoningRitual.DATA.ID);
        }
        else
        {
            GameActions.Bottom.MakeCardInDrawPile(new Crystallize());
        }
    }

    @Override
    protected String GetUpdatedDescription()
    {
        String description = powerStrings.DESCRIPTIONS[0];
        int[] thresholds = GetThresholds();
        Integer threshold = GetCurrentThreshold();
        if (threshold != null)
        {
            final String card = (threshold == thresholds[thresholds.length - 1])
                    ? (JUtils.ModifyString(SummoningRitual.DATA.Strings.NAME, w -> "#p" + w))
                    : ("#y" + Crystallize.DATA.Strings.NAME);
            description = JUtils.Format(description + powerStrings.DESCRIPTIONS[1], threshold, card);
        }
        else
        {
            description = JUtils.Format(description, 0, 1, "");
        }

        return description;
    }
}