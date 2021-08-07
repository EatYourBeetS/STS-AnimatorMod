package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Blue extends AffinityToken
{
    public static final EYBCardData DATA = Register(AffinityToken_Blue.class);
    public static final AffinityType AFFINITY_TYPE = AffinityType.Blue;
    public static final AffinityType AFFINITY_REQ1 = AffinityType.Green;
    public static final AffinityType AFFINITY_REQ2 = AffinityType.Dark;

    public AffinityToken_Blue()
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