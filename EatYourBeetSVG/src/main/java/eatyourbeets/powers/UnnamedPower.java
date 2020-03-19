package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.resources.GR;

public abstract class UnnamedPower extends EYBPower
{
    public static String CreateFullID(Class<? extends UnnamedPower> type)
    {
        return GR.Unnamed.CreateID(type.getSimpleName());
    }

    public UnnamedPower(AbstractCreature owner, String id)
    {
        super(owner, id);
    }
}
