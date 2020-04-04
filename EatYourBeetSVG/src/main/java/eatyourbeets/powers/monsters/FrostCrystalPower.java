package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.utilities.GameActions;

public class FrostCrystalPower extends AbstractCrystalPower
{
    public static final String POWER_ID = CreateFullID(FrostCrystalPower.class);

    public FrostCrystalPower(AbstractCreature owner, int value)
    {
        super(POWER_ID, owner, value);
    }

    @Override
    protected void Activate(AbstractCreature target)
    {
        GameActions.Bottom.StackPower(null, new PlatedArmorPower(target, amount));
    }
}