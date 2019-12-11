package eatyourbeets.actions.orbs;

import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameActions;

public class AetherOrbEvokeAction extends EYBAction
{
    public AetherOrbEvokeAction(int cardDraw)
    {
        super(ActionType.DRAW);

        Initialize(cardDraw);
    }

    @Override
    protected void FirstUpdate()
    {
        GameActions.Bottom.Draw(amount);
        Complete();
    }
}