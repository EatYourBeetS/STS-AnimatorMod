package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;

public class TemporaryDesecrationPower extends AbstractTemporaryPower
{
    public static final String POWER_ID = CreateFullID(TemporaryDesecrationPower.class);

    public TemporaryDesecrationPower(AbstractCreature owner, int amount)
    {
        super(owner, amount, POWER_ID, DesecrationPower::new);
    }
}