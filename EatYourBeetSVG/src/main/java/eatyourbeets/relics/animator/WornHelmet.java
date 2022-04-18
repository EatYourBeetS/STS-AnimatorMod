package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class WornHelmet extends AnimatorRelic
{
    public static final String ID = CreateFullID(WornHelmet.class);
    public static final int BLOCK_AMOUNT1 = 4;
    public static final int BLOCK_AMOUNT2 = 1;

    public WornHelmet()
    {
        super(ID, RelicTier.COMMON, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, BLOCK_AMOUNT1, BLOCK_AMOUNT2);
    }

    @Override
    public void atBattleStart()
    {
        GameActions.Bottom.Add(new RelicAboveCreatureAction(player, this));
        GameActions.Bottom.GainBlock(BLOCK_AMOUNT1);
        SetCounter(0);
        flash();
    }

    @Override
    public void onRefreshHand()
    {
        super.onRefreshHand();

        SetCounter(JUtils.Count(player.hand.group, GameUtilities::IsHindrance));
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        int block = JUtils.Count(player.hand.group, GameUtilities::IsHindrance) * BLOCK_AMOUNT2;
        if (block > 0)
        {
            GameActions.Bottom.GainBlock(block);
            flash();
        }
    }
}