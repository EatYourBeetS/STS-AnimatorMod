package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public abstract class UnnamedPower extends BasePower
{
    public static String CreateFullID(String id)
    {
        return "unnamed:" + id;
    }

    public UnnamedPower(AbstractCreature owner, String id)
    {
        super(owner, id);
    }
}
