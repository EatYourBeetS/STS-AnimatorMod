package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class Strike_Green extends ImprovedStrike
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Green;
    public static final EYBCardData DATA = Register(Strike_Green.class);

    public Strike_Green()
    {
        super(DATA, AFFINITY_TYPE);
    }
}