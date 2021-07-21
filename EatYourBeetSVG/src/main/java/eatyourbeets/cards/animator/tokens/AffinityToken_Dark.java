package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Dark extends AffinityToken
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Dark;
    public static final EYBCardData DATA = Register(AffinityToken_Dark.class);

    public AffinityToken_Dark()
    {
        super(DATA, AFFINITY_TYPE);
    }
}