package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.relics.animator.EngravedStaff;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ForcePower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(ForcePower.class.getSimpleName());

    public ForcePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        GameActions.Bottom.GainStrength(amount);
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        GameActions.Bottom.GainStrength(stackAmount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        if (amount <= 2 && EffectHistory.HasActivatedLimited(EngravedStaff.ID))
        {
            return;
        }

        if (GameUtilities.GetStrength() > 0)
        {
            GameActions.Bottom.ReducePower(owner, StrengthPower.POWER_ID, 1);
        }

        ReducePower(1);
    }
}