package pinacolada.powers.orbs;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import pinacolada.orbs.pcl.Fire;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLJUtils;

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
        return PCLJUtils.SafeCast(orb, Fire.class);
    }
}