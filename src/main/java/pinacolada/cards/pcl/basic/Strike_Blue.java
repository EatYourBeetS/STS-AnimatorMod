package pinacolada.cards.pcl.basic;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class Strike_Blue extends ImprovedStrike
{
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Blue;
    public static final PCLCardData DATA = Register(Strike_Blue.class);

    public Strike_Blue()
    {
        super(DATA, AFFINITY_TYPE);
    }
}