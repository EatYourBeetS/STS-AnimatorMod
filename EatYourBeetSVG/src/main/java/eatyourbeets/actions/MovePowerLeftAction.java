package eatyourbeets.actions;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MovePowerLeftAction extends AnimatorAction
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