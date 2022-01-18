package pinacolada.powers.temporary;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.DexterityPower;
import pinacolada.powers.common.AbstractTemporaryPower;

public class TemporaryDexterityPower extends AbstractTemporaryPower
{
    public static final String POWER_ID = CreateFullID(TemporaryDexterityPower.class);

    public TemporaryDexterityPower(AbstractCreature owner, int amount)
    {
        super(owner, amount, POWER_ID, DexterityPower::new);
    }
}