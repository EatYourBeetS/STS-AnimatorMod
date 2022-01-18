package pinacolada.cards.pcl.basic;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class Strike_Red extends ImprovedStrike
{
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Red;
    public static final PCLCardData DATA = Register(Strike_Red.class);

    public Strike_Red()
    {
        super(DATA, AFFINITY_TYPE);
    }
}