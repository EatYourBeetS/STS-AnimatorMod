package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.replacement.AnimatorPlatedArmorPower;
import eatyourbeets.utilities.GameActions;

public class FrostCrystalPower extends AbstractCrystalPower
{
    public static final String POWER_ID = CreateFullID(FrostCrystalPower.class);

    public FrostCrystalPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);
    }

    @Override
    protected void Activate(AbstractCreature target)
    {
        GameActions.Bottom.StackPower(null, new AnimatorPlatedArmorPower(target, amount));
    }
}