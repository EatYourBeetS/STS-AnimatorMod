package eatyourbeets.monsters.SharedMoveset_Old;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.utilities.GameUtilities;

public class Move_GainTempThornsAll extends EYBAbstractMove
{
    private final int buffAmount;

    public Move_GainTempThornsAll(int buffAmount)
    {
        this.buffAmount = buffAmount;
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.BUFF);
    }

    public void QueueActions(AbstractCreature target)
    {
        for (AbstractMonster m : GameUtilities.GetAllEnemies(true))
        {
            GameActions.Bottom.StackPower(owner, new EarthenThornsPower(m, buffAmount));
        }
    }
}
