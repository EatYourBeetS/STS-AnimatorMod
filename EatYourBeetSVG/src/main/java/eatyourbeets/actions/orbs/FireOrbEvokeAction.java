package eatyourbeets.actions.orbs;

import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class FireOrbEvokeAction extends EYBAction
{
    public FireOrbEvokeAction(int burning)
    {
        super(ActionType.DEBUFF);

        Initialize(burning);
    }

    @Override
    protected void FirstUpdate()
    {
        if (this.amount > 0)
        {
            GameActions.Bottom.ApplyBurning(TargetHelper.Enemies(null), amount);
        }

        Complete();
    }
}
