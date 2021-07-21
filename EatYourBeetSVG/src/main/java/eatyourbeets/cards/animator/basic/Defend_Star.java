package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Star extends ImprovedDefend
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Star;
    public static final EYBCardData DATA = Register(Defend_Star.class);

    public Defend_Star()
    {
        super(DATA, AFFINITY_TYPE);
    }
}