package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Red extends AffinityToken
{
    public static final EYBCardData DATA = Register(AffinityToken_Red.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Red;
    public static final Affinity AFFINITY_REQ1 = Affinity.Green;
    public static final Affinity AFFINITY_REQ2 = Affinity.Light;

    public AffinityToken_Red()
    {
        super(DATA, AFFINITY_TYPE);
    }

    @Override
    protected Affinity GetAffinityRequirement1()
    {
        return AFFINITY_REQ1;
    }

    @Override
    protected Affinity GetAffinityRequirement2()
    {
        return AFFINITY_REQ2;
    }
}