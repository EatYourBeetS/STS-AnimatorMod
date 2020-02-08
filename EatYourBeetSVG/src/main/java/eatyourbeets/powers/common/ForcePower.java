package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;

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
    public float GetScaling(EYBCard card)
    {
        return card.forceScaling * amount;
    }

    @Override
    protected void OnThresholdReached()
    {
        GameActions.Top.GainStrength(1);
    }
}