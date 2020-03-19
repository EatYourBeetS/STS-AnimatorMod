package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;

public abstract class AnimatorPower extends EYBPower
{
    public static String CreateFullID(Class<? extends AnimatorPower> type)
    {
        return GR.Animator.CreateID(type.getSimpleName());
    }

    public AnimatorPower(AbstractCreature owner, EYBCardData cardData)
    {
        super(owner, cardData);
    }

    public AnimatorPower(AbstractCreature owner, String id)
    {
        super(owner, id);
    }
}
