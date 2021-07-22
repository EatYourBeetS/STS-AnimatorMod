package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Red extends AffinityToken
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Red;
    public static final EYBCardData DATA = Register(AffinityToken_Red.class);

    public AffinityToken_Red()
    {
        super(DATA, AFFINITY_TYPE);
    }
}