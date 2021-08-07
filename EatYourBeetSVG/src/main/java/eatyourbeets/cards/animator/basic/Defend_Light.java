package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Light extends ImprovedDefend
{
    public static final Affinity AFFINITY_TYPE = Affinity.Light;
    public static final EYBCardData DATA = Register(Defend_Light.class);

    public Defend_Light()
    {
        super(DATA, AFFINITY_TYPE);
    }
}