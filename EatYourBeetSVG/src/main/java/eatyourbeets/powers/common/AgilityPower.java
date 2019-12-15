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
    public boolean preserveOnce = false;

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

        GameActions.Top.GainDexterity(amount);
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        GameActions.Top.GainDexterity(stackAmount);
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

        if (GameUtilities.GetDexterity() > 0)
        {
            GameActions.Bottom.ReducePower(owner, DexterityPower.POWER_ID, 1);
        }

        GameActions.Bottom.ReducePower(this, 1);
    }
}