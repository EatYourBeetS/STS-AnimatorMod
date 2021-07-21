package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Light extends ImprovedDefend
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Light;
    public static final EYBCardData DATA = Register(Defend_Light.class);

    public Defend_Light()
    {
        super(DATA, AFFINITY_TYPE);
    }
}