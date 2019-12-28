package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ForcePower extends PlayerAttributePower
{
    public static final String POWER_ID = CreateFullID(ForcePower.class.getSimpleName());

    public ForcePower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);
    }

    public static void PreserveOnce()
    {
        preservedPowers.Subscribe(POWER_ID);
    }

    @Override
    protected void GainPower(int amount)
    {
        GameActions.Top.GainStrength(amount);
    }

    @Override
    protected void ReducePower(int amount)
    {
        if (GameUtilities.GetStrength() > 0)
        {
            GameActions.Top.ReducePower(owner, StrengthPower.POWER_ID, 1);
        }
    }
}