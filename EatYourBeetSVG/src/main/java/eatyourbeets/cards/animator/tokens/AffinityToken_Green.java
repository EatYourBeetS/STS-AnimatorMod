package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Green extends AffinityToken
{
    public static final EYBCardData DATA = RegisterAffinityToken(AffinityToken_Green.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Green;

    public AffinityToken_Green()
    {
        super(DATA, AFFINITY_TYPE);
    }
}