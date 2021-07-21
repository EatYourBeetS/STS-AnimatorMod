package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Orange extends ImprovedDefend
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Orange;
    public static final EYBCardData DATA = Register(Defend_Orange.class);

    public Defend_Orange()
    {
        super(DATA, AFFINITY_TYPE);
    }
}