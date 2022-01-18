package pinacolada.powers.temporary;

import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.powers.common.AbstractTemporaryPower;
import pinacolada.powers.common.EnchantedArmorPower;

public class TemporaryEnchantedArmorPower extends AbstractTemporaryPower
{
    public static final String POWER_ID = CreateFullID(TemporaryEnchantedArmorPower.class);

    public TemporaryEnchantedArmorPower(AbstractCreature owner, int amount)
    {
        super(owner, amount, POWER_ID, EnchantedArmorPower::new);
    }
}
