package pinacolada.cards.pcl.basic;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class Strike_Silver extends ImprovedStrike
{
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Silver;
    public static final PCLCardData DATA = Register(Strike_Silver.class);

    public Strike_Silver()
    {
        super(DATA, AFFINITY_TYPE);
    }
}