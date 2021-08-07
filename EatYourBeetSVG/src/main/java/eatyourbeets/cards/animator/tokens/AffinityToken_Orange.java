package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Orange extends AffinityToken
{
    public static final EYBCardData DATA = Register(AffinityToken_Orange.class);
    public static final AffinityType AFFINITY_TYPE = AffinityType.Orange;
    public static final AffinityType AFFINITY_REQ1 = AffinityType.Blue;
    public static final AffinityType AFFINITY_REQ2 = AffinityType.Light;

    public AffinityToken_Orange()
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