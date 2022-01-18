package pinacolada.cards.pcl.basic;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class Defend_Orange extends ImprovedDefend
{
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Orange;
    public static final PCLCardData DATA = Register(Defend_Orange.class);

    public Defend_Orange()
    {
        super(DATA, AFFINITY_TYPE);
    }
}