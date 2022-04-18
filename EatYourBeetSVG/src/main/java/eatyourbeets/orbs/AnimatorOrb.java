package eatyourbeets.orbs;

import eatyourbeets.resources.GR;
import eatyourbeets.resources.common.CommonImages;

public abstract class AnimatorOrb extends EYBOrb
{
    public static final CommonImages.Orbs IMAGES = GR.Common.Images.Orbs;

    public static String CreateFullID(Class<? extends AnimatorOrb> type)
    {
        return GR.Animator.CreateID(type.getSimpleName());
    }

    public AnimatorOrb(String id, Timing passiveEffectTiming)
    {
        super(id, passiveEffectTiming);
    }
}
