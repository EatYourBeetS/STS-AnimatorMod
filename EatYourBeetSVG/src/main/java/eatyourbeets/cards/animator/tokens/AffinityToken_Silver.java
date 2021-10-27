package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Silver extends AffinityToken //TODO add additional effect
{
    public static final EYBCardData DATA = RegisterAffinityToken(AffinityToken_Silver.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Silver;

    public AffinityToken_Silver()
    {
        super(DATA, AFFINITY_TYPE);
    }
}