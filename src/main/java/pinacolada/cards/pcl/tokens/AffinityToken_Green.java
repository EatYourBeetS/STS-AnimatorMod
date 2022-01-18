package pinacolada.cards.pcl.tokens;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class AffinityToken_Green extends AffinityToken
{
    public static final PCLCardData DATA = RegisterAffinityToken(AffinityToken_Green.class);
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Green;

    public AffinityToken_Green()
    {
        super(DATA, AFFINITY_TYPE);
    }
}