package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Orange extends ImprovedDefend
{
    public static final Affinity AFFINITY_TYPE = Affinity.Earth;
    public static final EYBCardData DATA = Register(Defend_Orange.class);

    public Defend_Orange()
    {
        super(DATA, AFFINITY_TYPE);
    }
}