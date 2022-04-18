package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Strike_Dark extends ImprovedStrike
{
    public static final Affinity AFFINITY_TYPE = Affinity.Dark;
    public static final EYBCardData DATA = Register(Strike_Dark.class);

    public Strike_Dark()
    {
        super(DATA, AFFINITY_TYPE);
    }
}