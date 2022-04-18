package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HealingCubePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(HealingCubePower.class);

    public HealingCubePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.priority = -99;

        Initialize(amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        if (owner.isPlayer)
        {
            GameActions.Bottom.Heal(owner, owner, amount);
        }
        else for (AbstractCreature m : GameUtilities.GetEnemies(true))
        {
            GameActions.Bottom.Heal(null, m, amount);
        }
    }
}
