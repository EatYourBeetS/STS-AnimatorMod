package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Dark extends AffinityToken
{
    public static final EYBCardData DATA = Register(AffinityToken_Dark.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Dark;
    public static final Affinity AFFINITY_REQ1 = Affinity.Light;
    public static final Affinity AFFINITY_REQ2 = Affinity.Orange;

    public AffinityToken_Dark()
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