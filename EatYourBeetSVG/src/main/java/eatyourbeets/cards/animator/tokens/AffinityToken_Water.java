package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Water extends AffinityToken
{
    public static final EYBCardData DATA = RegisterAffinityToken(AffinityToken_Water.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Water;

    public AffinityToken_Water()
    {
        super(DATA, AFFINITY_TYPE);
    }
}