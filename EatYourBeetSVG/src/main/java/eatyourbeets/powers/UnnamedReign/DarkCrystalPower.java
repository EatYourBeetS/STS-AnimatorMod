package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;

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
        GameActionsHelper.ApplyPower(null, target, new ConstrictedPower(target, null, amount), amount);
    }
}
