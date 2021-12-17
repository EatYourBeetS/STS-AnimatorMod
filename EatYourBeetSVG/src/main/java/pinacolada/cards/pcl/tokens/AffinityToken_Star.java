package pinacolada.cards.pcl.tokens;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;

public class AffinityToken_Star extends AffinityToken //TODO add additional effect
{
    public static final PCLCardData DATA = RegisterAffinityToken(AffinityToken_Star.class);
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Star;

    public AffinityToken_Star()
    {
        super(DATA, AFFINITY_TYPE);
    }
}