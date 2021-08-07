package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Light extends AffinityToken
{
    public static final EYBCardData DATA = Register(AffinityToken_Light.class);
    public static final AffinityType AFFINITY_TYPE = AffinityType.Light;
    public static final AffinityType AFFINITY_REQ1 = AffinityType.Dark;
    public static final AffinityType AFFINITY_REQ2 = AffinityType.Red;

    public AffinityToken_Light()
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