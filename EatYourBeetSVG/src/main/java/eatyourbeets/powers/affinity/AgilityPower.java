package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;

public class AgilityPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(AgilityPower.class);
    public static final AffinityType AFFINITY_TYPE = AffinityType.Green;

    public AgilityPower(AbstractCreature owner, int amount)
    {
        super(AFFINITY_TYPE, POWER_ID, owner, amount);
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