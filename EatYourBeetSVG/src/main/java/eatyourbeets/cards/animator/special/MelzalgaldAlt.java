package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;

public abstract class MelzalgaldAlt extends AnimatorCard
{
    public MelzalgaldAlt(EYBCardData data)
    {
        super(data);

        SetExhaust(true);
        SetSeries(CardSeries.OnePunchMan);
        SetAffinity_Star(1, 1);
    }
}