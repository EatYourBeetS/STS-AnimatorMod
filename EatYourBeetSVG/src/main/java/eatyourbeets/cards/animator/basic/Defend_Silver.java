package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Silver extends ImprovedDefend
{
    public static final Affinity AFFINITY_TYPE = Affinity.Silver;
    public static final EYBCardData DATA = Register(Defend_Silver.class);

    public Defend_Silver()
    {
        super(DATA, AFFINITY_TYPE);
    }
}