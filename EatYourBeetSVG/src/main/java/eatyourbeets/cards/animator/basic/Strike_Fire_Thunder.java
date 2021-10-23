package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Strike_Fire_Thunder extends ImprovedStrike
{
    public static final Affinity AFFINITY_TYPE_1 = Affinity.Fire;
    public static final Affinity AFFINITY_TYPE_2 = Affinity.Thunder;
    public static final EYBCardData DATA = Register(Strike_Fire_Thunder.class);

    public Strike_Fire_Thunder()
    {
        super(DATA, AFFINITY_TYPE_1, AFFINITY_TYPE_2);
    }
}