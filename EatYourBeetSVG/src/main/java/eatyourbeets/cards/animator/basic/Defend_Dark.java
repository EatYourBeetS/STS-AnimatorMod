package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Dark extends ImprovedDefend
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Dark;
    public static final EYBCardData DATA = Register(Defend_Dark.class);

    public Defend_Dark()
    {
        super(DATA, AFFINITY_TYPE);
    }
}