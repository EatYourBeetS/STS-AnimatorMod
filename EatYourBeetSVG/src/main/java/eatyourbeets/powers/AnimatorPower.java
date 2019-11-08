package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public abstract class AnimatorPower extends BasePower
{
    public static String CreateFullID(String id)
    {
        return "animator:" + id;
    }

    public AnimatorPower(AbstractCreature owner, String id)
    {
        super(owner, id);
    }
}
