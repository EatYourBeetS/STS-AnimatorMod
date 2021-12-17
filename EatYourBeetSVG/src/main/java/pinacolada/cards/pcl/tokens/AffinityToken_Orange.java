package pinacolada.cards.pcl.tokens;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class AffinityToken_Orange extends AffinityToken
{
    public static final PCLCardData DATA = RegisterAffinityToken(AffinityToken_Orange.class);
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Orange;

    public AffinityToken_Orange()
    {
        super(DATA, AFFINITY_TYPE);
    }
}