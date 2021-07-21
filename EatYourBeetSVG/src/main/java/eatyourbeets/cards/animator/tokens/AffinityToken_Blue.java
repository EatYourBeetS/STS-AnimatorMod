package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Blue extends AffinityToken
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Blue;
    public static final EYBCardData DATA = Register(AffinityToken_Blue.class);

    public AffinityToken_Blue()
    {
        super(DATA, AFFINITY_TYPE);
    }
}