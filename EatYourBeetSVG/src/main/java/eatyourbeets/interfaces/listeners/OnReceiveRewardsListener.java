package eatyourbeets.interfaces.listeners;

import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

public interface OnReceiveRewardsListener
{
    void OnReceiveRewards(ArrayList<RewardItem> rewards);
}
