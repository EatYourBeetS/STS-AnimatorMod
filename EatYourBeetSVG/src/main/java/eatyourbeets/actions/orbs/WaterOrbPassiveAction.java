package eatyourbeets.actions.orbs;

import eatyourbeets.actions.EYBAction;
import eatyourbeets.orbs.animator.Water;

public class WaterOrbPassiveAction extends EYBAction
{
    protected final Water orb;

    public WaterOrbPassiveAction(Water orb, int increase)
    {
        super(ActionType.DEBUFF);

        this.orb = orb;

        Initialize(increase);
    }

    @Override
    protected void FirstUpdate()
    {
        orb.evokeAmount += amount;
        Complete();
    }
}