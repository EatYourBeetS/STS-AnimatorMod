package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class Strike_Red extends ImprovedStrike
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Red;
    public static final EYBCardData DATA = Register(Strike_Red.class);

    public Strike_Red()
    {
        super(DATA, AFFINITY_TYPE);
    }
}