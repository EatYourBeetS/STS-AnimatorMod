package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.resources.animator.AnimatorResources;

public abstract class AnimatorPower extends BasePower
{
    public static String CreateFullID(String id)
    {
        return AnimatorResources.CreateID(id);
    }

    public AnimatorPower(AbstractCreature owner, String id)
    {
        super(owner, id);
    }
}
