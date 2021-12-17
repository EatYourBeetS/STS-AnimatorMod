package pinacolada.cards.pcl.basic;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class Defend_Green extends ImprovedDefend
{
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Green;
    public static final PCLCardData DATA = Register(Defend_Green.class);

    public Defend_Green()
    {
        super(DATA, AFFINITY_TYPE);
    }
}