package pinacolada.powers.temporary;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import pinacolada.powers.common.AbstractTemporaryPower;

public class TemporaryStrengthPower extends AbstractTemporaryPower
{
    public static final String POWER_ID = CreateFullID(TemporaryStrengthPower.class);

    public TemporaryStrengthPower(AbstractCreature owner, int amount)
    {
        super(owner, amount, POWER_ID, StrengthPower::new);
    }
}