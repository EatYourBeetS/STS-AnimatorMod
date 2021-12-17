package pinacolada.cards.pcl.basic;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class Strike_Star extends ImprovedStrike
{
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Star;
    public static final PCLCardData DATA = Register(Strike_Star.class);

    public Strike_Star()
    {
        super(DATA, AFFINITY_TYPE);
    }
}