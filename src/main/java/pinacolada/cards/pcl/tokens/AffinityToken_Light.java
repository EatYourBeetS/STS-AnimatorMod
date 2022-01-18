package pinacolada.cards.pcl.tokens;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class AffinityToken_Light extends AffinityToken
{
    public static final PCLCardData DATA = RegisterAffinityToken(AffinityToken_Light.class);
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Light;

    public AffinityToken_Light()
    {
        super(DATA, AFFINITY_TYPE);
    }
}