package eatyourbeets.cards.animator;

import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.interfaces.Hidden;

public abstract class MelzalgaldAlt extends AnimatorCard implements Hidden
{
    public static final String ID = CreateFullID(MelzalgaldAlt.class.getSimpleName());

    public MelzalgaldAlt(String id)
    {
        super(Resources_Animator.GetCardStrings(id), id, Resources_Animator.GetCardImage(id), 1, CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF_AND_ENEMY);

        this.exhaust = true;
    }
}