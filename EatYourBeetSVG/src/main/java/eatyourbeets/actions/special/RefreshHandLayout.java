package eatyourbeets.actions.special;

import eatyourbeets.actions.EYBAction;

public class RefreshHandLayout extends EYBAction
{
    public RefreshHandLayout()
    {
        super(ActionType.SPECIAL);
    }

    @Override
    protected void FirstUpdate()
    {
        player.hand.refreshHandLayout();
        player.hand.glowCheck();
        player.hand.applyPowers();

        Complete();
    }
}
