package eatyourbeets.actions.orbs;

import eatyourbeets.actions.EYBAction;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.utilities.GameActions;

public class EarthOrbPassiveAction extends EYBAction
{
    public EarthOrbPassiveAction(int damage)
    {
        super(ActionType.DEBUFF);

        Initialize(damage);
    }

    @Override
    protected void FirstUpdate()
    {
        GameActions.Bottom.StackPower(new EarthenThornsPower(player, amount));

        Complete();
    }
}