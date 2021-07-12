package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;

public class AgilityPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(AgilityPower.class);

    public static void PreserveOnce()
    {
        preservedPowers.Subscribe(POWER_ID);
    }

    public AgilityPower(AbstractCreature owner, int amount)
    {
        super(AffinityType.Green, POWER_ID, owner, amount);
    }

    @Override
    public float GetScaling(EYBCard card)
    {
        return card.agilityScaling * amount;
    }

    @Override
    protected void OnThresholdReached(int threshold)
    {
        GameActions.Top.GainDexterity(1);
    }
}