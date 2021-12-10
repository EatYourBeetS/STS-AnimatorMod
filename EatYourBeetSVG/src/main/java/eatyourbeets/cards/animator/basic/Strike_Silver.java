package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Strike_Silver extends ImprovedStrike
{
    public static final Affinity AFFINITY_TYPE = Affinity.Silver;
    public static final EYBCardData DATA = Register(Strike_Silver.class);

    public Strike_Silver()
    {
        super(DATA, AFFINITY_TYPE);
    }
}