package pinacolada.cards.pcl.basic;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class Defend_Star extends ImprovedDefend
{
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Star;
    public static final PCLCardData DATA = Register(Defend_Star.class);

    public Defend_Star()
    {
        super(DATA, AFFINITY_TYPE);
    }
}