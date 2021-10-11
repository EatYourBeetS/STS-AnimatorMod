package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Strike_Green extends ImprovedStrike
{
    public static final Affinity AFFINITY_TYPE = Affinity.Air;
    public static final EYBCardData DATA = Register(Strike_Green.class);

    public Strike_Green()
    {
        super(DATA, AFFINITY_TYPE);
    }
}