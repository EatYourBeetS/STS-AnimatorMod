package pinacolada.cards.pcl.tokens;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class AffinityToken_Blue extends AffinityToken
{
    public static final PCLCardData DATA = RegisterAffinityToken(AffinityToken_Blue.class);
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Blue;

    public AffinityToken_Blue()
    {
        super(DATA, AFFINITY_TYPE);
    }
}