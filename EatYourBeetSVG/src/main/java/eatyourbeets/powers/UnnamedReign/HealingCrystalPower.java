package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.utilities.GameActions;

public class HealingCrystalPower extends AbstractCrystalPower
{
    public static final String POWER_ID = CreateFullID(HealingCrystalPower.class.getSimpleName());

    public HealingCrystalPower(AbstractCreature owner, int value)
    {
        super(POWER_ID, owner, value);
    }

    @Override
    protected void Activate(AbstractCreature target)
    {
        GameActions.Bottom.Heal(null, target, amount);
    }
}
