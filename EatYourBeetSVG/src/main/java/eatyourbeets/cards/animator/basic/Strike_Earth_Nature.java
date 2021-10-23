package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Strike_Earth_Nature extends ImprovedStrike
{
    public static final Affinity AFFINITY_TYPE_1 = Affinity.Earth;
    public static final Affinity AFFINITY_TYPE_2 = Affinity.Nature;
    public static final EYBCardData DATA = Register(Strike_Earth_Nature.class);

    public Strike_Earth_Nature()
    {
        super(DATA, AFFINITY_TYPE_1, AFFINITY_TYPE_2);
    }
}