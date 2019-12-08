package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;

public class DelayedHealPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(DelayedHealPower.class.getSimpleName());

    public DelayedHealPower(AbstractCreature owner, int stacks)
    {
        super(owner, POWER_ID);
        this.amount = stacks;
        this.type = PowerType.BUFF;
        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        GameActionsHelper.AddToBottom(new HealAction(owner, owner, amount));
        GameActionsHelper.AddToBottom(new RemoveSpecificPowerAction(owner, owner, this));
    }
}