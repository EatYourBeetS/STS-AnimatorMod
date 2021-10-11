package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Fire extends AffinityToken
{
    public static final EYBCardData DATA = RegisterAffinityToken(AffinityToken_Fire.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Fire;

    public AffinityToken_Fire()
    {
        super(DATA, AFFINITY_TYPE);
    }
}