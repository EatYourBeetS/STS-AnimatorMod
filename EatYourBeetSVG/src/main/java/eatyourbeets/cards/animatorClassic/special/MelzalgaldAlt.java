package eatyourbeets.cards.animatorClassic.special;

import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;

public abstract class MelzalgaldAlt extends AnimatorClassicCard
{
    public MelzalgaldAlt(EYBCardData data)
    {
        super(data);

        SetExhaust(true);
        this.series = CardSeries.OnePunchMan;
        SetShapeshifter();
    }
}