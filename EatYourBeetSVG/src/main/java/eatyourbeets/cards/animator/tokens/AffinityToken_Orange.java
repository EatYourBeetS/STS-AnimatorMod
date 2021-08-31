package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Orange extends AffinityToken
{
    public static final EYBCardData DATA = RegisterAffinityToken(AffinityToken_Orange.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Orange;

    public AffinityToken_Orange()
    {
        super(DATA, AFFINITY_TYPE);
    }
}