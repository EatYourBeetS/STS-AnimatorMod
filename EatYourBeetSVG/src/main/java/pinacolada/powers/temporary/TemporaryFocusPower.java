package pinacolada.powers.temporary;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.FocusPower;
import pinacolada.powers.common.AbstractTemporaryPower;

public class TemporaryFocusPower extends AbstractTemporaryPower
{
    public static final String POWER_ID = CreateFullID(TemporaryFocusPower.class);

    public TemporaryFocusPower(AbstractCreature owner, int amount)
    {
        super(owner, amount, POWER_ID, FocusPower::new);
    }
}