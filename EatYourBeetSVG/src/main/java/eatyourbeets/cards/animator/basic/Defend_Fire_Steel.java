package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Fire_Steel extends ImprovedDefend
{
    public static final Affinity AFFINITY_TYPE_1 = Affinity.Fire;
    public static final Affinity AFFINITY_TYPE_2 = Affinity.Steel;
    public static final EYBCardData DATA = Register(Defend_Fire_Steel.class);

    public Defend_Fire_Steel()
    {
        super(DATA, AFFINITY_TYPE_1, AFFINITY_TYPE_2);
    }
}