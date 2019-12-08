package eatyourbeets.powers.common;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;

public class TemporaryDrawReductionPower extends DrawReductionPower implements CloneablePowerInterface
{
    public TemporaryDrawReductionPower(AbstractCreature owner, int amount)
    {
        super(owner, amount);

        this.ID = AnimatorPower.CreateFullID(TemporaryDrawReductionPower.class.getSimpleName());
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        GameActionsHelper.AddToBottom(new ReducePowerAction(owner, owner, this, 1));
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new TemporaryDrawReductionPower(owner, amount);
    }
}
