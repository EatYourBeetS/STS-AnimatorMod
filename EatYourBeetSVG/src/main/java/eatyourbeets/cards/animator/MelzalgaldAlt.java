package eatyourbeets.cards.animator;

import eatyourbeets.AnimatorResources;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.interfaces.Hidden;

public abstract class MelzalgaldAlt extends AnimatorCard implements Hidden
{
    public static final String ID = CreateFullID(MelzalgaldAlt.class.getSimpleName());

    public MelzalgaldAlt(String id)
    {
        super(AnimatorResources.GetCardStrings(id), id, AnimatorResources.GetCardImage(id), 1, CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF_AND_ENEMY);

        this.exhaust = true;
    }
}