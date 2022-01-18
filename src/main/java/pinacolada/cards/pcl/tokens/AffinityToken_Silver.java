package pinacolada.cards.pcl.tokens;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class AffinityToken_Silver extends AffinityToken //TODO add additional effect
{
    public static final PCLCardData DATA = RegisterAffinityToken(AffinityToken_Silver.class);
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Silver;

    public AffinityToken_Silver()
    {
        super(DATA, AFFINITY_TYPE);
    }
}