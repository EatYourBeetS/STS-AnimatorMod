package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.interfaces.markers.Hidden;

public abstract class MelzalgaldAlt extends AnimatorCard implements Hidden
{
    public static final String ID = Register(MelzalgaldAlt.class);

    public MelzalgaldAlt(String id)
    {
        super(GetStaticData(id), id, AnimatorResources.GetCardImage(id), 1, CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF_AND_ENEMY);

        SetExhaust(true);
        SetSynergy(Synergies.OnePunchMan, true);
    }
}