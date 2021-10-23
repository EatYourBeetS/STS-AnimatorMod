package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Strike_Mind_Cyber extends ImprovedStrike
{
    public static final Affinity AFFINITY_TYPE_1 = Affinity.Mind;
    public static final Affinity AFFINITY_TYPE_2 = Affinity.Cyber;
    public static final EYBCardData DATA = Register(Strike_Mind_Cyber.class);

    public Strike_Mind_Cyber()
    {
        super(DATA, AFFINITY_TYPE_1, AFFINITY_TYPE_2);
    }
}