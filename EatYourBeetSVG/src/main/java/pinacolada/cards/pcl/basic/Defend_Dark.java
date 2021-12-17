package pinacolada.cards.pcl.basic;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class Defend_Dark extends ImprovedDefend
{
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Dark;
    public static final PCLCardData DATA = Register(Defend_Dark.class);

    public Defend_Dark()
    {
        super(DATA, AFFINITY_TYPE);
    }
}