package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.effects.player.RemoveRelicEffect;
import eatyourbeets.interfaces.OnReceiveRewardsSubscriber;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.relics.AnimatorRelic;

import java.util.ArrayList;

public class Destiny extends AnimatorRelic implements OnReceiveRewardsSubscriber, Hidden
{
    public static final String ID = CreateFullID(Destiny.class.getSimpleName());

    public Destiny()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public int getPrice()
    {
        return 600;
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void OnReceiveRewards(ArrayList<RewardItem> rewards)
    {
        AbstractDungeon.effectsQueue.add(new RemoveRelicEffect(this, this));
    }
}