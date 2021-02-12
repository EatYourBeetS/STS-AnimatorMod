package eatyourbeets.powers.orbs;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

public class FireAffinityPower extends OrbAffinityPower<Fire>
{
    public static final String POWER_ID = CreateFullID(FireAffinityPower.class);

    public FireAffinityPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, GR.Tooltips.Fire, amount);
    }

    @Override
    public Fire Validate(AbstractOrb orb)
    {
        return JUtils.SafeCast(orb, Fire.class);
    }
}