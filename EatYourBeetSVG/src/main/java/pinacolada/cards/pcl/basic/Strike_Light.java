package pinacolada.cards.pcl.basic;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class Strike_Light extends ImprovedStrike
{
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Light;
    public static final PCLCardData DATA = Register(Strike_Light.class);

    public Strike_Light()
    {
        super(DATA, AFFINITY_TYPE);
    }
}