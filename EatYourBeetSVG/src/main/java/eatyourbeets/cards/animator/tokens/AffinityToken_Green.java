package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Green extends AffinityToken
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Green;
    public static final EYBCardData DATA = Register(AffinityToken_Green.class);

    public AffinityToken_Green()
    {
        super(DATA, AFFINITY_TYPE);
    }
}