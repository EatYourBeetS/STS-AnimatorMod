package pinacolada.cards.pcl.basic;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class Defend_Red extends ImprovedDefend
{
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Red;
    public static final PCLCardData DATA = Register(Defend_Red.class);

    public Defend_Red()
    {
        super(DATA, AFFINITY_TYPE);
    }
}