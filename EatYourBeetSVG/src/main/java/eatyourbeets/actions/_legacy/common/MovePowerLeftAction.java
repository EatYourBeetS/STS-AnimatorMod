package eatyourbeets.actions._legacy.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MovePowerLeftAction extends AbstractGameAction
{
    private final String powerID;

    public MovePowerLeftAction(AbstractCreature target, String powerID)
    {
        this.target = target;
        this.powerID = powerID;
    }

    public void update()
    {
        AbstractPower toMove = target.getPower(powerID);
        if (toMove != null)
        {
            target.powers.remove(toMove);
            target.powers.add(0, toMove);
        }

        this.isDone = true;
    }
}