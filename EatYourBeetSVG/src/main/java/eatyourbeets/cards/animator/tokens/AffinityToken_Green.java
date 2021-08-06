package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Green extends AffinityToken
{
    public static final EYBCardData DATA = Register(AffinityToken_Green.class);
    public static final AffinityType AFFINITY_TYPE = AffinityType.Green;
    public static final AffinityType AFFINITY_REQ1 = AffinityType.Red;
    public static final AffinityType AFFINITY_REQ2 = AffinityType.Blue;

    public AffinityToken_Green()
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