package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Strike_Air_Steel extends ImprovedStrike
{
    public static final Affinity AFFINITY_TYPE_1 = Affinity.Air;
    public static final Affinity AFFINITY_TYPE_2 = Affinity.Steel;
    public static final EYBCardData DATA = Register(Strike_Air_Steel.class);

    public Strike_Air_Steel()
    {
        super(DATA, AFFINITY_TYPE_1, AFFINITY_TYPE_2);
    }
}