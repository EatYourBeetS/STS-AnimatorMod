package eatyourbeets.monsters.SharedMoveset_Old;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.powers.animator.EarthenThornsPower;

public class Move_GainTempThorns extends EYBAbstractMove
{
    private final int amount;

    public Move_GainTempThorns(int amount)
    {
        this.amount = amount;
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.BUFF);
    }

    public void QueueActions(AbstractCreature target)
    {
        GameActions.Bottom.StackPower(new EarthenThornsPower(owner, amount));
    }
}
