package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Air_Water extends ImprovedDefend
{
    public static final Affinity AFFINITY_TYPE_1 = Affinity.Air;
    public static final Affinity AFFINITY_TYPE_2 = Affinity.Water;
    public static final EYBCardData DATA = Register(Defend_Air_Water.class);

    public Defend_Air_Water()
    {
        super(DATA, AFFINITY_TYPE_1, AFFINITY_TYPE_2);
    }
}