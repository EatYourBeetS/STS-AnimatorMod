package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;

public abstract class MelzalgaldAlt extends AnimatorCard
{
    public MelzalgaldAlt(EYBCardData data)
    {
        super(data);

        SetExhaust(true);
        SetSynergy(Synergies.OnePunchMan);
        SetShapeshifter();
        SetAffinity(1, 1, 1, 0, 2);
    }
}