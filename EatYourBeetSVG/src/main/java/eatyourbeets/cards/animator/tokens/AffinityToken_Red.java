package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Red extends AffinityToken
{
    public static final EYBCardData DATA = RegisterAffinityToken(AffinityToken_Red.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Red;

    public AffinityToken_Red()
    {
        super(DATA, AFFINITY_TYPE);
    }
}