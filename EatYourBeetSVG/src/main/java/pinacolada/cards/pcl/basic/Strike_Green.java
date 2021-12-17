package pinacolada.cards.pcl.basic;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class Strike_Green extends ImprovedStrike
{
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Green;
    public static final PCLCardData DATA = Register(Strike_Green.class);

    public Strike_Green()
    {
        super(DATA, AFFINITY_TYPE);
    }
}