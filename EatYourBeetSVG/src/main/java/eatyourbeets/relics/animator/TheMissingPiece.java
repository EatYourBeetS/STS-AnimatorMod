package eatyourbeets.relics.animator;

import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.utilities.GameUtilities;
import org.apache.commons.lang3.StringUtils;

import java.util.StringJoiner;

public class TheMissingPiece extends AbstractMissingPiece
{
    public static final String ID = CreateFullID(TheMissingPiece.class.getSimpleName());

    private static final int REWARD_INTERVAL = 4;

    public static void RefreshDescription()
    {
        TheMissingPiece missingPiece = GameUtilities.GetRelic(TheMissingPiece.class);
        if (missingPiece != null)
        {
            if (missingPiece.tips.size() > 0)
            {
                missingPiece.tips.get(0).body = missingPiece.getUpdatedDescription();
                missingPiece.flash();
            }
        }
    }

    public TheMissingPiece()
    {
        super(ID, RelicTier.STARTER, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        String base = super.getUpdatedDescription();
        if (GR.Animator.Dungeon.Series.isEmpty())
        {
            return base;
        }

        StringJoiner joiner = new StringJoiner(" NL ");
        for (AnimatorRuntimeLoadout series : GR.Animator.Dungeon.Series)
        {
            if (series.promoted)
            {
                String line = "- #y" + StringUtils.replace(series.Loadout.Name," ", " #y");
                if (series.bonus > 0)
                {
                    line += " #y( " + series.bonus + "/6 #y)";
                }

                joiner.add(line);
            }
            else
            {
                joiner.add("- " + series.Loadout.Name);
            }
        }

        return base + " NL  NL " + DESCRIPTIONS[1] + " NL " + joiner.toString();
    }

    @Override
    protected int GetRewardInterval()
    {
        return REWARD_INTERVAL;
    }
}