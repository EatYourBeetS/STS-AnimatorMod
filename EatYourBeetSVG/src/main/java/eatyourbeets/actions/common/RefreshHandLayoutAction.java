package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.animator.AnimatorAction;

public class RefreshHandLayoutAction extends AnimatorAction
{
    public RefreshHandLayoutAction()
    {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.SPECIAL;
    }

    public void update()
    {
        CardGroup hand = AbstractDungeon.player.hand;

        hand.refreshHandLayout();
        hand.glowCheck();
        hand.applyPowers();

        this.isDone = true;
    }
}
