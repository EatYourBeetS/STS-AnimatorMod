package eatyourbeets.orbs;

import eatyourbeets.resources.GR;

public abstract class AnimatorOrb extends EYBOrb
{
    public static String CreateFullID(Class<? extends AnimatorOrb> type)
    {
        return GR.Animator.CreateID(type.getSimpleName());
    }

    public AnimatorOrb(String id, Timing passiveEffectTiming)
    {
        super(id, passiveEffectTiming);
    }
}
