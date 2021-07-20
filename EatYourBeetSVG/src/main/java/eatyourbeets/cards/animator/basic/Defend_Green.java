package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Green extends ImprovedDefend
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Green;
    public static final EYBCardData DATA = Register(Defend_Green.class);

    public Defend_Green()
    {
        super(DATA, AFFINITY_TYPE);
    }
}