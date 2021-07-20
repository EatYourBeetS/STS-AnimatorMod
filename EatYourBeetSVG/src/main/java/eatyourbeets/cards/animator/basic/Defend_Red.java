package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Red extends ImprovedDefend
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Red;
    public static final EYBCardData DATA = Register(Defend_Red.class);

    public Defend_Red()
    {
        super(DATA, AFFINITY_TYPE);
    }
}