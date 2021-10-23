package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Strike_Light_Water extends ImprovedStrike
{
    public static final Affinity AFFINITY_TYPE_1 = Affinity.Light;
    public static final Affinity AFFINITY_TYPE_2 = Affinity.Water;
    public static final EYBCardData DATA = Register(Strike_Light_Water.class);

    public Strike_Light_Water()
    {
        super(DATA, AFFINITY_TYPE_1, AFFINITY_TYPE_2);
    }
}