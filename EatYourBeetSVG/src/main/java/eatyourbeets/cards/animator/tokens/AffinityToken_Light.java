package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Light extends AffinityToken
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Light;
    public static final EYBCardData DATA = Register(AffinityToken_Light.class);

    public AffinityToken_Light()
    {
        super(DATA, AFFINITY_TYPE);
    }
}