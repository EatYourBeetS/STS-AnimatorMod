package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class Strike_Light extends ImprovedStrike
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Light;
    public static final EYBCardData DATA = Register(Strike_Light.class);

    public Strike_Light()
    {
        super(DATA, AFFINITY_TYPE);
    }
}