package pinacolada.powers.orbs;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLJUtils;

public class FrostAffinityPower extends OrbAffinityPower<Frost>
{
    public static final String POWER_ID = CreateFullID(FrostAffinityPower.class);

    public FrostAffinityPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, GR.Tooltips.Frost, amount);
    }

    @Override
    public Frost Validate(AbstractOrb orb)
    {
        return PCLJUtils.SafeCast(orb, Frost.class);
    }
}