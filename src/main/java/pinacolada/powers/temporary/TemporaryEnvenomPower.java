package pinacolada.powers.temporary;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.EnvenomPower;
import pinacolada.powers.common.AbstractTemporaryPower;

public class TemporaryEnvenomPower extends AbstractTemporaryPower
{
    public static final String POWER_ID = CreateFullID(TemporaryEnvenomPower.class);

    public TemporaryEnvenomPower(AbstractCreature owner, int amount)
    {
        super(owner, amount, POWER_ID, EnvenomPower::new);
    }
}
