package eatyourbeets.interfaces;

import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

public interface OnReceiveRewardsSubscriber
{
    void OnReceiveRewards(ArrayList<RewardItem> rewards);
}
