package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HealingCubePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(HealingCubePower.class.getSimpleName());

    public HealingCubePower(AbstractCreature owner, int value)
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
            GameActions.Bottom.Heal(owner, owner, amount);
        }
        else for (AbstractCreature m : GameUtilities.GetAllEnemies(true))
        {
            GameActions.Bottom.Heal(null, m, amount);
        }
    }
}
