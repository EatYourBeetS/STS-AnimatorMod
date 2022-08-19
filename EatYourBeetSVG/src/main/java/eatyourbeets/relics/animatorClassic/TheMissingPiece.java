package eatyourbeets.relics.animatorClassic;

import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

public class TheMissingPiece extends AbstractMissingPiece
{
    public static final String ID = CreateFullID(TheMissingPiece.class);
    public static final int REWARD_INTERVAL = 4;

    public TheMissingPiece()
    {
        super(ID, RelicTier.STARTER, LandingSound.FLAT);
    }

    @Override
    protected int GetRewardInterval()
    {
        return REWARD_INTERVAL;
    }

    @Override
    public void OnReceiveRewards(ArrayList<RewardItem> rewards, boolean normalRewards)
    {

    }
}