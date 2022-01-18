package pinacolada.cards.pcl.basic;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class Strike_Orange extends ImprovedStrike
{
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Orange;
    public static final PCLCardData DATA = Register(Strike_Orange.class);

    public Strike_Orange()
    {
        super(DATA, AFFINITY_TYPE);
    }
}