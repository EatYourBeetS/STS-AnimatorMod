package eatyourbeets.rewards.neow;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.neow.NeowReward;
import eatyourbeets.relics.animator.NeowsComplaint;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;

public class EYBNeowReward extends NeowReward
{
    private static FieldInfo<Boolean> _activated = JUtils.GetField("activated", NeowReward.class);

    public EYBNeowReward(boolean firstMini)
    {
        super(firstMini);

        if (type == NeowRewardType.THREE_ENEMY_KILL)
        {
            SetText("Your first 3 card rewards contain 1 additional card.");
        }
    }

    public EYBNeowReward(int category)
    {
        super(category);

        if (type == NeowRewardType.THREE_ENEMY_KILL)
        {
            SetText("Your first 3 card rewards contain 1 additional card.");
        }
    }

    @Override
    public void update()
    {
        super.update();
    }

    @Override
    public void activate()
    {
        if (type == NeowRewardType.THREE_ENEMY_KILL)
        {
            _activated.Set(this, true);
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), new NeowsComplaint());
            CardCrawlGame.metricData.addNeowData(this.type.name(), this.drawback.name());
        }
        else
        {
            super.activate();
        }
    }

    protected void SetText(String text)
    {
        this.optionLabel = "[ " + JUtils.ModifyString(text, w -> "#g" + w) + " ]";
    }
}
