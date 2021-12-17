package pinacolada.cards.pcl.tokens;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class AffinityToken_Red extends AffinityToken
{
    public static final PCLCardData DATA = RegisterAffinityToken(AffinityToken_Red.class);
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Red;

    public AffinityToken_Red()
    {
        super(DATA, AFFINITY_TYPE);
    }
}