package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.GameUtilities;

public class FireCubePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(FireCubePower.class);

    public FireCubePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.priority = -99;

        Initialize(amount);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, amount * 3);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        if (owner.isPlayer)
        {
            GameActions.Bottom.GainStrength(amount);
            GameActions.Bottom.StackPower(null, new BurningPower(owner, null, amount * 3))
            .ShowEffect(false, true)
            .IgnoreArtifact(true);
        }
        else for (AbstractCreature m : GameUtilities.GetEnemies(true))
        {
            GameActions.Bottom.StackPower(null, new StrengthPower(m, amount));
            GameActions.Bottom.StackPower(null, new BurningPower(m, null, amount * 3))
            .ShowEffect(false, true)
            .IgnoreArtifact(true);
        }
    }
}
