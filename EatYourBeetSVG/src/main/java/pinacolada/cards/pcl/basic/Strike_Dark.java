package pinacolada.cards.pcl.basic;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class Strike_Dark extends ImprovedStrike
{
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Dark;
    public static final PCLCardData DATA = Register(Strike_Dark.class);

    public Strike_Dark()
    {
        super(DATA, AFFINITY_TYPE);
    }
}