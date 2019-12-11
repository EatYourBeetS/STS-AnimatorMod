package eatyourbeets.actions.orbs;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

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
            for (AbstractMonster m : GameUtilities.GetCurrentEnemies(true))
            {
                GameActions.Bottom.ApplyBurning(source, m, amount);
            }
        }

        Complete();
    }
}
