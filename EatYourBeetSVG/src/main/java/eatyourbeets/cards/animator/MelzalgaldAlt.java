package eatyourbeets.cards.animator;

import eatyourbeets.cards.Synergies;
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.interfaces.markers.Hidden;

public abstract class MelzalgaldAlt extends AnimatorCard implements Hidden
{
    public static final String ID = Register(MelzalgaldAlt.class.getSimpleName());

    public MelzalgaldAlt(String id)
    {
        super(staticCardData.get(id), id, Resources_Animator.GetCardImage(id), 1, CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF_AND_ENEMY);

        SetExhaust(true);
        SetSynergy(Synergies.OnePunchMan, true);
    }
}