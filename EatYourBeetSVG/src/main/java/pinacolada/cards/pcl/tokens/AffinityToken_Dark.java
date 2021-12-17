package pinacolada.cards.pcl.tokens;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class AffinityToken_Dark extends AffinityToken
{
    public static final PCLCardData DATA = RegisterAffinityToken(AffinityToken_Dark.class);
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Dark;

    public AffinityToken_Dark()
    {
        super(DATA, AFFINITY_TYPE);
    }
}