package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class Strike_Dark extends ImprovedStrike
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Dark;
    public static final EYBCardData DATA = Register(Strike_Dark.class);

    public Strike_Dark()
    {
        super(DATA, AFFINITY_TYPE);
    }
}