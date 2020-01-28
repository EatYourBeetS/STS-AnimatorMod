package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IntellectPower extends PlayerAttributePower
{
    public static final String POWER_ID = CreateFullID(IntellectPower.class.getSimpleName());

    public IntellectPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);
    }

    @Override
    protected float GetScaling(EYBCard card)
    {
        return card.forceScaling * amount;
    }

    public static void PreserveOnce()
    {
        preservedPowers.Subscribe(POWER_ID);
    }

    @Override
    protected void GainPower(int amount)
    {
        GameActions.Top.GainFocus(amount);
    }

    @Override
    protected void ReducePower(int amount)
    {
        if (GameUtilities.GetFocus() > 0)
        {
            GameActions.Top.ReducePower(owner, FocusPower.POWER_ID, 1);
        }
    }
}