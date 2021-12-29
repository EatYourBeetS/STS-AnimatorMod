package pinacolada.powers.temporary;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ThornsPower;
import pinacolada.powers.common.AbstractTemporaryPower;

public class TemporaryThornsPower extends AbstractTemporaryPower
{
    public static final String POWER_ID = CreateFullID(TemporaryThornsPower.class);

    public TemporaryThornsPower(AbstractCreature owner, int amount)
    {
        super(owner, amount, POWER_ID, ThornsPower::new);
    }
}
