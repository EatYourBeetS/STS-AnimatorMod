package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.resources.GR;

public abstract class AnimatorPower extends BasePower
{
    public static String CreateFullID(String id)
    {
        return GR.Animator.CreateID(id);
    }

    public AnimatorPower(AbstractCreature owner, String id)
    {
        super(owner, id);
    }
}
