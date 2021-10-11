package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Air extends AffinityToken
{
    public static final EYBCardData DATA = RegisterAffinityToken(AffinityToken_Air.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Air;

    public AffinityToken_Air()
    {
        super(DATA, AFFINITY_TYPE);
    }
}