package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Green extends AffinityToken
{
    public static final EYBCardData DATA = Register(AffinityToken_Green.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Green;
    public static final Affinity AFFINITY_REQ1 = Affinity.Red;
    public static final Affinity AFFINITY_REQ2 = Affinity.Blue;

    public AffinityToken_Green()
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