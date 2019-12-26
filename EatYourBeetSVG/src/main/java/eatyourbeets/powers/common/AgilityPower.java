package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.DexterityPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class AgilityPower extends PlayerAttributePower
{
    public static final String POWER_ID = CreateFullID(AgilityPower.class.getSimpleName());

    public AgilityPower(AbstractCreature owner, int amount)
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
        GameActions.Top.GainDexterity(amount);
    }

    @Override
    protected void ReducePower(int amount)
    {
        if (GameUtilities.GetDexterity() > 0)
        {
            GameActions.Bottom.ReducePower(owner, DexterityPower.POWER_ID, 1);
        }
    }
}