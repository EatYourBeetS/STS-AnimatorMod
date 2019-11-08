package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.DexterityPower;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.relics.animator.EngravedStaff;
import eatyourbeets.utilities.GameActionsHelper;

public class AgilityPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(AgilityPower.class.getSimpleName());

    public AgilityPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        GameActionsHelper.ApplyPower(owner, owner, new DexterityPower(owner, amount), amount);
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        GameActionsHelper.ApplyPower(owner, owner, new DexterityPower(owner, stackAmount), stackAmount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurnPostDraw();

        if (amount <= 2 && PlayerStatistics.HasActivatedLimited(EngravedStaff.ID))
        {
            return;
        }

        if (PlayerStatistics.GetDexterity() > 0)
        {
            GameActionsHelper.AddToBottom(new ReducePowerAction(owner, owner, DexterityPower.POWER_ID, 1));
        }

        GameActionsHelper.AddToBottom(new ReducePowerAction(owner, owner, this, 1));
    }
}
