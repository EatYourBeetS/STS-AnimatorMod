package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Earth extends AffinityToken
{
    public static final EYBCardData DATA = RegisterAffinityToken(AffinityToken_Earth.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Earth;

    public AffinityToken_Earth()
    {
        super(DATA, AFFINITY_TYPE);
    }
}