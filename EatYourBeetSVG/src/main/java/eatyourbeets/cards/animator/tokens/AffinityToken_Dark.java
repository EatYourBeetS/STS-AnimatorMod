package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;

public class AffinityToken_Dark extends AffinityToken
{
    public static final EYBCardData DATA = RegisterAffinityToken(AffinityToken_Dark.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Dark;

    public AffinityToken_Dark()
    {
        super(DATA, AFFINITY_TYPE);
    }
}