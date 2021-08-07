package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Light extends AffinityToken
{
    public static final EYBCardData DATA = Register(AffinityToken_Light.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Light;
    public static final Affinity AFFINITY_REQ1 = Affinity.Dark;
    public static final Affinity AFFINITY_REQ2 = Affinity.Red;

    public AffinityToken_Light()
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