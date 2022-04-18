package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Strike_Blue extends ImprovedStrike
{
    public static final Affinity AFFINITY_TYPE = Affinity.Blue;
    public static final EYBCardData DATA = Register(Strike_Blue.class);

    public Strike_Blue()
    {
        super(DATA, AFFINITY_TYPE);
    }
}