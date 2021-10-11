package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Red extends ImprovedDefend
{
    public static final Affinity AFFINITY_TYPE = Affinity.Fire;
    public static final EYBCardData DATA = Register(Defend_Red.class);

    public Defend_Red()
    {
        super(DATA, AFFINITY_TYPE);
    }
}