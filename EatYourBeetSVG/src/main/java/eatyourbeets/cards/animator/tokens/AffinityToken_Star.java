package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Star extends AffinityToken //TODO add additional effect
{
    public static final EYBCardData DATA = RegisterAffinityToken(AffinityToken_Star.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Star;

    public AffinityToken_Star()
    {
        super(DATA, AFFINITY_TYPE);
    }
}