package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Blue extends ImprovedDefend
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Blue;
    public static final EYBCardData DATA = Register(Defend_Blue.class);

    public Defend_Blue()
    {
        super(DATA, AFFINITY_TYPE);
    }
}