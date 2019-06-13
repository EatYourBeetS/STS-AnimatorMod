package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.utilities.GameActionsHelper;

public class FrostCrystalPower extends AbstractCrystalPower
{
    public static final String POWER_ID = CreateFullID(FrostCrystalPower.class.getSimpleName());

    public FrostCrystalPower(AbstractCreature owner, int value)
    {
        super(POWER_ID, owner, value);
    }

    @Override
    protected void Activate(AbstractCreature target)
    {
        GameActionsHelper.ApplyPower(null, target, new PlatedArmorPower(target, amount), amount);
    }
}