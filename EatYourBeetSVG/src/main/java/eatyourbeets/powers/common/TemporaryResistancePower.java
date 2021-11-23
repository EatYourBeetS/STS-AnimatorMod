package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;

public class TemporaryResistancePower extends AbstractTemporaryPower
{
    public static final String POWER_ID = CreateFullID(TemporaryResistancePower.class);

    public TemporaryResistancePower(AbstractCreature owner, int amount)
    {
        super(owner, amount, POWER_ID, ResistancePower::new);
    }
}