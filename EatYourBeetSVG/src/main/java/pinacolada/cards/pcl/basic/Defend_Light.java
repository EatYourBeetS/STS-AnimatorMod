package pinacolada.cards.pcl.basic;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class Defend_Light extends ImprovedDefend
{
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Light;
    public static final PCLCardData DATA = Register(Defend_Light.class);

    public Defend_Light()
    {
        super(DATA, AFFINITY_TYPE);
    }
}