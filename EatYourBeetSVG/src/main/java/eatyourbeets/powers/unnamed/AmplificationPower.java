package eatyourbeets.powers.unnamed;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.UnnamedPower;
import eatyourbeets.utilities.Mathf;

public class AmplificationPower extends UnnamedPower
{
    public static final String POWER_ID = CreateFullID(AmplificationPower.class);

    public AmplificationPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount, PowerType.DEBUFF, false);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type)
    {
        return super.atDamageReceive(type == DamageInfo.DamageType.NORMAL ? Mathf.Ceil(damage * ((amount + 100) / 100f)) : damage, type);
    }
}
