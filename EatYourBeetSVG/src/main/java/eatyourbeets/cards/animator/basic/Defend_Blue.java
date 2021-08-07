package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Blue extends ImprovedDefend
{
    public static final Affinity AFFINITY_TYPE = Affinity.Blue;
    public static final EYBCardData DATA = Register(Defend_Blue.class);

    public Defend_Blue()
    {
        super(DATA, AFFINITY_TYPE);
    }
}