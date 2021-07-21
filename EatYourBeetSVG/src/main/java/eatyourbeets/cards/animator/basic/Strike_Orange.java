package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class Strike_Orange extends ImprovedStrike
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Orange;
    public static final EYBCardData DATA = Register(Strike_Orange.class);

    public Strike_Orange()
    {
        super(DATA, AFFINITY_TYPE);
    }
}