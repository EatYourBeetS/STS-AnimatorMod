package pinacolada.cards.pcl.basic;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class Defend_Blue extends ImprovedDefend
{
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Blue;
    public static final PCLCardData DATA = Register(Defend_Blue.class);

    public Defend_Blue()
    {
        super(DATA, AFFINITY_TYPE);
    }
}