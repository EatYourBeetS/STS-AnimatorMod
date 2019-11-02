package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.animator.AnimatorAction;
import eatyourbeets.utilities.GameActionsHelper;

public class RemoveRightmostDebuffAction extends AnimatorAction
{
    public RemoveRightmostDebuffAction(AbstractCreature target)
    {
        this.target = target;
    }

    public void update()
    {
        for (int i = target.powers.size() - 1; i >= 0; i--)
        {
            AbstractPower power = target.powers.get(i);
            if (power.type == AbstractPower.PowerType.DEBUFF)
            {
                GameActionsHelper.AddToBottom(new RemoveSpecificPowerAction(target, target, power));
                break;
            }
        }

        this.isDone = true;
    }
}