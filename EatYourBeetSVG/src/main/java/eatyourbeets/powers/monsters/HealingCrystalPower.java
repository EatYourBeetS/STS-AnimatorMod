package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.utilities.GameActions;

public class HealingCrystalPower extends AbstractCrystalPower
{
    public static final String POWER_ID = CreateFullID(HealingCrystalPower.class);

    public HealingCrystalPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);
    }

    @Override
    protected void Activate(AbstractCreature target)
    {
        GameActions.Bottom.Heal(null, target, amount);
    }
}
