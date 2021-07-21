package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class Strike_Star extends ImprovedStrike
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Star;
    public static final EYBCardData DATA = Register(Strike_Star.class);

    public Strike_Star()
    {
        super(DATA, AFFINITY_TYPE);
    }
}