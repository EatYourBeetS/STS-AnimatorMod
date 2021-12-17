package pinacolada.cards.pcl.basic;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class Defend_Silver extends ImprovedDefend
{
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Silver;
    public static final PCLCardData DATA = Register(Defend_Silver.class);

    public Defend_Silver()
    {
        super(DATA, AFFINITY_TYPE);
    }
}