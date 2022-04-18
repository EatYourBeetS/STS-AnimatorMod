package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class DarkCubePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(DarkCubePower.class);

    public DarkCubePower(AbstractCreature owner, int amount)
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
            GameActions.Bottom.ApplyConstricted(TargetHelper.Enemies(owner), amount);
        }
        else
        {
            GameActions.Bottom.ApplyConstricted(null, player, amount);
        }

        this.flash();
    }
}
