package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.relics.animator.EngravedStaff;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IntellectPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(IntellectPower.class.getSimpleName());
    public boolean preserveOnce = false;

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

        GameActions.Bottom.GainFocus(amount);
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        GameActions.Bottom.GainFocus(stackAmount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        if (preserveOnce)
        {
            preserveOnce = false;

            return;
        }

        if (amount <= 2 && EffectHistory.HasActivatedLimited(EngravedStaff.ID))
        {
            return;
        }

        if (GameUtilities.GetFocus() > 0)
        {
            GameActions.Bottom.ReducePower(owner, FocusPower.POWER_ID, 1);
        }

        ReducePower(1);
    }
}