package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Light_Thunder extends ImprovedDefend
{
    public static final Affinity AFFINITY_TYPE_1 = Affinity.Light;
    public static final Affinity AFFINITY_TYPE_2 = Affinity.Thunder;
    public static final EYBCardData DATA = Register(Defend_Light_Thunder.class);

    public Defend_Light_Thunder()
    {
        super(DATA, AFFINITY_TYPE_1, AFFINITY_TYPE_2);
    }
}