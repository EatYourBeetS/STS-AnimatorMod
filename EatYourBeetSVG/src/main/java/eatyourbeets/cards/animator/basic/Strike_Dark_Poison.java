package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Strike_Dark_Poison extends ImprovedStrike
{
    public static final Affinity AFFINITY_TYPE_1 = Affinity.Dark;
    public static final Affinity AFFINITY_TYPE_2 = Affinity.Poison;
    public static final EYBCardData DATA = Register(Strike_Dark_Poison.class);

    public Strike_Dark_Poison()
    {
        super(DATA, AFFINITY_TYPE_1, AFFINITY_TYPE_2);
    }
}