package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.utilities.GameActions;

public class DarkCrystalPower extends AbstractCrystalPower
{
    public static final String POWER_ID = CreateFullID(DarkCrystalPower.class.getSimpleName());

    public DarkCrystalPower(AbstractCreature owner, int value)
    {
        super(POWER_ID, owner, value);
    }

    @Override
    protected void Activate(AbstractCreature target)
    {
        GameActions.Bottom.ApplyConstricted(null, target, amount);
    }
}
