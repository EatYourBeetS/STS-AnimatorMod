package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;

public class IntellectPower extends PlayerAttributePower
{
    public static final String POWER_ID = CreateFullID(IntellectPower.class);

    public static int GetCurrentLevel()
    {
        return GetLevel(IntellectPower.class);
    }

    public static void PreserveOnce()
    {
        preservedPowers.Subscribe(POWER_ID);
    }

    public IntellectPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);
    }

    @Override
    protected float GetScaling(EYBCard card)
    {
        return card.intellectScaling * amount;
    }

    @Override
    protected void OnThresholdReached()
    {
        GameActions.Top.GainFocus(1);
    }
}