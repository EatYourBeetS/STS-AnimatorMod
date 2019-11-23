package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.relics.animator.EngravedStaff;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.GameUtilities;

public class IntellectPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(IntellectPower.class.getSimpleName());

    public IntellectPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.priority = Integer.MAX_VALUE;
        this.amount = amount;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        GameActionsHelper.ApplyPower(owner, owner, new FocusPower(owner, amount), amount);
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        GameActionsHelper.ApplyPower(owner, owner, new FocusPower(owner, stackAmount), stackAmount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurnPostDraw();

        if (amount <= 2 && EffectHistory.HasActivatedLimited(EngravedStaff.ID))
        {
            return;
        }

        if (GameUtilities.GetFocus() > 0)
        {
            GameActionsHelper.AddToBottom(new ReducePowerAction(owner, owner, FocusPower.POWER_ID, 1));
        }

        GameActionsHelper.AddToBottom(new ReducePowerAction(owner, owner, this, 1));
    }
}
