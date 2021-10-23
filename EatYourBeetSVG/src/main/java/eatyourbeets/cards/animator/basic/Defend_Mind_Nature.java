package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Mind_Nature extends ImprovedDefend
{
    public static final Affinity AFFINITY_TYPE_1 = Affinity.Mind;
    public static final Affinity AFFINITY_TYPE_2 = Affinity.Nature;
    public static final EYBCardData DATA = Register(Defend_Mind_Nature.class);

    public Defend_Mind_Nature()
    {
        super(DATA, AFFINITY_TYPE_1, AFFINITY_TYPE_2);
    }
}