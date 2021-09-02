package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Light extends AffinityToken
{
    public static final EYBCardData DATA = RegisterAffinityToken(AffinityToken_Light.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Light;

    public AffinityToken_Light()
    {
        super(DATA, AFFINITY_TYPE);
    }
}