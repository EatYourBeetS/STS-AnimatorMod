package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Strike_Light extends ImprovedStrike
{
    public static final Affinity AFFINITY_TYPE = Affinity.Light;
    public static final EYBCardData DATA = Register(Strike_Light.class);

    public Strike_Light()
    {
        super(DATA, AFFINITY_TYPE);
    }
}