package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Strike_Orange extends ImprovedStrike
{
    public static final Affinity AFFINITY_TYPE = Affinity.Earth;
    public static final EYBCardData DATA = Register(Strike_Orange.class);

    public Strike_Orange()
    {
        super(DATA, AFFINITY_TYPE);
    }
}