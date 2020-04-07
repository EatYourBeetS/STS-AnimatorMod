package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameUtilities;

public class FrostCubePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(FrostCubePower.class);

    public FrostCubePower(AbstractCreature owner, int value)
    {
        super(owner, POWER_ID);

        this.amount = value;
        this.priority = -99;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + amount + desc[1];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        if (owner.isPlayer)
        {
            GameActions.Bottom.GainPlatedArmor(amount);
        }
        else for (AbstractCreature m : GameUtilities.GetEnemies(true))
        {
            GameActions.Bottom.StackPower(null, new PlatedArmorPower(m, amount));
        }
    }
}
