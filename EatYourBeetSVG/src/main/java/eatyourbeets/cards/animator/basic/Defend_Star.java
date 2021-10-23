package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Star extends ImprovedDefend
{
    public static final Affinity AFFINITY_TYPE1 = Affinity.Star;
    public static final EYBCardData DATA = RegisterSpecial(Defend_Star.class);

    public Defend_Star()
    {
        super(DATA, AFFINITY_TYPE1, null);
    }
}