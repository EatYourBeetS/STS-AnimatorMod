package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class Defend_Dark_Cyber extends ImprovedDefend
{
    public static final Affinity AFFINITY_TYPE_1 = Affinity.Dark;
    public static final Affinity AFFINITY_TYPE_2 = Affinity.Cyber;
    public static final EYBCardData DATA = Register(Defend_Dark_Cyber.class);

    public Defend_Dark_Cyber()
    {
        super(DATA, AFFINITY_TYPE_1, AFFINITY_TYPE_2);
    }
}