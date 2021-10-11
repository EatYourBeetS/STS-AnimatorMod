package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Strike_Red extends ImprovedStrike
{
    public static final Affinity AFFINITY_TYPE = Affinity.Fire;
    public static final EYBCardData DATA = Register(Strike_Red.class);

    public Strike_Red()
    {
        super(DATA, AFFINITY_TYPE);
    }
}