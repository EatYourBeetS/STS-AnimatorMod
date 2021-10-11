package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Green extends ImprovedDefend
{
    public static final Affinity AFFINITY_TYPE = Affinity.Air;
    public static final EYBCardData DATA = Register(Defend_Green.class);

    public Defend_Green()
    {
        super(DATA, AFFINITY_TYPE);
    }
}