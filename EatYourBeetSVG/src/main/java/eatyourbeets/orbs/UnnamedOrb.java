package eatyourbeets.orbs;

import eatyourbeets.resources.GR;

public abstract class UnnamedOrb extends EYBOrb
{
    public static String CreateFullID(Class<? extends UnnamedOrb> type)
    {
        return GR.Unnamed.CreateID(type.getSimpleName());
    }

    public UnnamedOrb(String id, Timing passiveEffectTiming)
    {
        super(id, passiveEffectTiming);
    }
}
