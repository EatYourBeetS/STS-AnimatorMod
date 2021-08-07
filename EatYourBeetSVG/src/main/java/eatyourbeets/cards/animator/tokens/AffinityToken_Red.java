package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Red extends AffinityToken
{
    public static final EYBCardData DATA = Register(AffinityToken_Red.class);
    public static final AffinityType AFFINITY_TYPE = AffinityType.Red;
    public static final AffinityType AFFINITY_REQ1 = AffinityType.Green;
    public static final AffinityType AFFINITY_REQ2 = AffinityType.Orange;

    public AffinityToken_Red()
    {
        super(DATA, AFFINITY_TYPE);
    }

    @Override
    protected AffinityType GetAffinityRequirement1()
    {
        return AFFINITY_REQ1;
    }

    @Override
    protected AffinityType GetAffinityRequirement2()
    {
        return AFFINITY_REQ2;
    }
}