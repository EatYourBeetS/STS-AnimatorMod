package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.rewards.RewardItem;

public interface OnAddingToCardReward
{
    boolean ShouldCancel(RewardItem rewardItem);
}