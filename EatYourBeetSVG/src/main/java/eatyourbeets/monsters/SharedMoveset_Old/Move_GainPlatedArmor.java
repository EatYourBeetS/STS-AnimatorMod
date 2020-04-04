package eatyourbeets.monsters.SharedMoveset_Old;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_GainPlatedArmor extends EYBAbstractMove
{
    private final int amount;

    public Move_GainPlatedArmor(int amount)
    {
        this.amount = amount;
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.BUFF);
    }

    public void QueueActions(AbstractCreature target)
    {
        GameActions.Bottom.ApplyPower(owner, owner, new PlatedArmorPower(owner, amount), amount);
    }
}
