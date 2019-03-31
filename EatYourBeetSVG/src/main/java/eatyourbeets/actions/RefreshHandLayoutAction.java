package eatyourbeets.actions;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RefreshHandLayoutAction extends AnimatorAction
{
    public RefreshHandLayoutAction()
    {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.SPECIAL;
    }

    public void update()
    {
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.hand.glowCheck();
        this.isDone = true;
    }
}
