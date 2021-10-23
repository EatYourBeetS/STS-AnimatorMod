package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Earth_Poison extends ImprovedDefend
{
    public static final Affinity AFFINITY_TYPE_1 = Affinity.Earth;
    public static final Affinity AFFINITY_TYPE_2 = Affinity.Poison;
    public static final EYBCardData DATA = Register(Defend_Earth_Poison.class);

    public Defend_Earth_Poison()
    {
        super(DATA, AFFINITY_TYPE_1, AFFINITY_TYPE_2);
    }
}