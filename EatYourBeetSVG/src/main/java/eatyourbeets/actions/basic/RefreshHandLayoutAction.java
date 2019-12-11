package eatyourbeets.actions.basic;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBAction;

public class RefreshHandLayoutAction extends EYBAction
{
    public RefreshHandLayoutAction()
    {
        super(ActionType.SPECIAL);
    }

    @Override
    protected void FirstUpdate()
    {
        CardGroup hand = AbstractDungeon.player.hand;

        hand.refreshHandLayout();
        hand.glowCheck();
        hand.applyPowers();

        Complete();
    }
}
