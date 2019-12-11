package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.DexterityPower;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.relics.animator.EngravedStaff;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

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

        GameActions.Bottom.GainDexterity(amount);
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        GameActions.Bottom.GainDexterity(stackAmount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurnPostDraw();

        if (amount <= 2 && EffectHistory.HasActivatedLimited(EngravedStaff.ID))
        {
            return;
        }

        if (GameUtilities.GetDexterity() > 0)
        {
            GameActions.Bottom.ReducePower(owner, DexterityPower.POWER_ID, 1);
        }

        ReducePower(1);
    }
}