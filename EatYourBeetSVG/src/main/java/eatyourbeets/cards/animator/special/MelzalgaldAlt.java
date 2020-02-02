package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.AnimatorResources;

public abstract class MelzalgaldAlt extends AnimatorCard
{
    public static final String ID = Register(MelzalgaldAlt.class);

    public MelzalgaldAlt(String id)
    {
        super(GetStaticData(id), id, AnimatorResources.GetCardImage(id), 1, CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF_AND_ENEMY);

        SetExhaust(true);
        SetSynergy(Synergies.OnePunchMan, true);
    }
}