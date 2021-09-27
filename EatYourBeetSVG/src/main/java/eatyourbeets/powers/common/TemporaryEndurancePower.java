package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;

public class TemporaryEndurancePower extends AbstractTemporaryPower
{
    public static final String POWER_ID = CreateFullID(TemporaryEndurancePower.class);

    public TemporaryEndurancePower(AbstractCreature owner, int amount)
    {
        super(owner, amount, POWER_ID, EndurancePower::new);
    }
}