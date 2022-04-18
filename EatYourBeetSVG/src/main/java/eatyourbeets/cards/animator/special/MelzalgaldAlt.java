package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.animator.series.OnePunchMan.Melzalgald;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;

public abstract class MelzalgaldAlt extends AnimatorCard
{
    protected final static CardSeries SERIES = Melzalgald.DATA.Series;

    public MelzalgaldAlt(EYBCardData data)
    {
        super(data);

        Initialize(4, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Star(2);

        SetRetainOnce(true);
        SetExhaust(true);

        SetAffinityRequirement(Affinity.General, 3);
    }
}