package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;

public class ForcePower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(ForcePower.class);
    public static final AffinityType AFFINITY_TYPE = AffinityType.Red;

    public static void PreserveOnce()
    {
        preservedPowers.Subscribe(POWER_ID);
    }

    public ForcePower(AbstractCreature owner, int amount)
    {
        super(AFFINITY_TYPE, POWER_ID, owner, amount);
    }

    @Override
    public float GetScaling(EYBCard card)
    {
        return card.forceScaling * amount;
    }

    @Override
    protected void OnThresholdReached(int threshold)
    {
        GameActions.Top.GainStrength(1);
    }
}