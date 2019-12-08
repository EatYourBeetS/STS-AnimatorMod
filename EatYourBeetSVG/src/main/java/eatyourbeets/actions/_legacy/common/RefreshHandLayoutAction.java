package eatyourbeets.actions._legacy.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RefreshHandLayoutAction extends AbstractGameAction
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
