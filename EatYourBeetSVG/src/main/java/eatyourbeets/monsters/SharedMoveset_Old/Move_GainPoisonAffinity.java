package eatyourbeets.monsters.SharedMoveset_Old;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.powers.animator.PoisonAffinityPower;

public class Move_GainPoisonAffinity extends EYBAbstractMove
{
    private final int amount;

    public Move_GainPoisonAffinity(int amount)
    {
        this.amount = amount;
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.BUFF);
    }

    public void QueueActions(AbstractCreature target)
    {
        GameActions.Bottom.ApplyPower(owner, owner, new PoisonAffinityPower(owner, amount), amount);
    }
}
