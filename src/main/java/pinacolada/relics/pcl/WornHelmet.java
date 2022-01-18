package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class WornHelmet extends PCLRelic
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
        PCLActions.Bottom.Add(new RelicAboveCreatureAction(player, this));
        PCLActions.Bottom.GainBlock(BLOCK_AMOUNT1);
        SetCounter(0);
        flash();
    }

    @Override
    public void onRefreshHand()
    {
        super.onRefreshHand();

        SetCounter(PCLJUtils.Count(player.hand.group, PCLGameUtilities::IsHindrance));
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        int block = PCLJUtils.Count(player.hand.group, PCLGameUtilities::IsHindrance) * BLOCK_AMOUNT2;
        if (block > 0)
        {
            PCLActions.Bottom.GainBlock(block);
            flash();
        }
    }
}