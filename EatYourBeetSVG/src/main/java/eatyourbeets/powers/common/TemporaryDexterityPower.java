package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class TemporaryDexterityPower extends AbstractTemporaryPower
{
    public static final String POWER_ID = CreateFullID(TemporaryDexterityPower.class);

    public TemporaryDexterityPower(AbstractCreature owner, int amount)
    {
        super(owner, amount, POWER_ID, DexterityPower::new);
    }
}