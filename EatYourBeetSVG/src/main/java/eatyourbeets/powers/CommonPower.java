package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.resources.GR;

public abstract class CommonPower extends EYBPower
{
    public static String CreateFullID(Class<? extends CommonPower> type)
    {
        return GR.Common.CreateID(type.getSimpleName());
    }

    public CommonPower(AbstractCreature owner, AbstractCreature source, String id)
    {
        super(owner, id);

        this.source = source;
    }

    public CommonPower(AbstractCreature owner, String id)
    {
        super(owner, id);
    }
}
