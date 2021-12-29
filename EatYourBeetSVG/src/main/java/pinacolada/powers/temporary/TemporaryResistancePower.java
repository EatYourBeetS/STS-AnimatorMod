package pinacolada.powers.temporary;

import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.powers.common.AbstractTemporaryPower;
import pinacolada.powers.common.ResistancePower;

public class TemporaryResistancePower extends AbstractTemporaryPower
{
    public static final String POWER_ID = CreateFullID(TemporaryResistancePower.class);

    public TemporaryResistancePower(AbstractCreature owner, int amount)
    {
        super(owner, amount, POWER_ID, ResistancePower::new);
    }
}