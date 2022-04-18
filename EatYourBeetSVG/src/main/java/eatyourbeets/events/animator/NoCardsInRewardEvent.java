package eatyourbeets.events.animator;

import com.megacrit.cardcrawl.events.AbstractEvent;

public class NoCardsInRewardEvent extends AbstractEvent
{
    public NoCardsInRewardEvent(boolean noCardsInReward)
    {
        this.noCardsInRewards = noCardsInReward;
    }

    @Override
    protected void buttonEffect(int i)
    {

    }
}