package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.utilities.GameActions;

public class DarkCrystalPower extends AbstractCrystalPower
{
    public static final String POWER_ID = CreateFullID(DarkCrystalPower.class);

    public DarkCrystalPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);
    }

    @Override
    protected void Activate(AbstractCreature target)
    {
        GameActions.Bottom.ApplyConstricted(null, target, amount);
    }
}
