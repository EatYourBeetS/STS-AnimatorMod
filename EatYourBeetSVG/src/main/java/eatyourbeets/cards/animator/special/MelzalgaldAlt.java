package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.animator.series.OnePunchMan.Melzalgald;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;

public abstract class MelzalgaldAlt extends AnimatorCard
{
    protected final static CardSeries SERIES = Melzalgald.DATA.Series;

    public MelzalgaldAlt(EYBCardData data)
    {
        super(data);

        SetAffinity_Star(1, 1, 1);

        SetExhaust(true);
    }
}