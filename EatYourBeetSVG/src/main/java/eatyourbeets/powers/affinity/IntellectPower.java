package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;

public class IntellectPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(IntellectPower.class);
    public static final AffinityType AFFINITY_TYPE = AffinityType.Blue;

    public static void PreserveOnce()
    {
        preservedPowers.Subscribe(POWER_ID);
    }

    public IntellectPower(AbstractCreature owner, int amount)
    {
        super(AFFINITY_TYPE, POWER_ID, owner, amount);
    }

    @Override
    protected float GetScaling(EYBCard card)
    {
        return card.intellectScaling * amount;
    }

    @Override
    protected void OnThresholdReached(int threshold)
    {
        GameActions.Top.GainFocus(1);
    }
}