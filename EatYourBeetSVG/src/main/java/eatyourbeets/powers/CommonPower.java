package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public abstract class CommonPower extends BasePower
{
    public static String CreateFullID(String id)
    {
        return "EYB:" + id;
    }

    public CommonPower(AbstractCreature owner, String id)
    {
        super(owner, id);
    }
}
