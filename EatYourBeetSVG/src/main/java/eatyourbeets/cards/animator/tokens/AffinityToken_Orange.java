package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Orange extends AffinityToken
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Orange;
    public static final EYBCardData DATA = Register(AffinityToken_Orange.class);

    public AffinityToken_Orange()
    {
        super(DATA, AFFINITY_TYPE);
    }
}