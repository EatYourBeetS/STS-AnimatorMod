package eatyourbeets.interfaces.listeners;

import com.megacrit.cardcrawl.rewards.RewardItem;

public interface OnAddingToCardRewardListener
{
    boolean ShouldCancel(RewardItem rewardItem);
}