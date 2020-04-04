package eatyourbeets.monsters.SharedMoveset_Old;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.utilities.GameActions;

public class Move_StrengthLoss extends EYBAbstractMove
{
    public final int amount;
    public boolean temporary;

    public Move_StrengthLoss(int amount, boolean temporary)
    {
        this.amount = amount;
        this.temporary = temporary;
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.STRONG_DEBUFF);
    }

    public void QueueActions(AbstractCreature target)
    {
        GameActions.Bottom.ReduceStrength(target, amount, temporary);
    }
}