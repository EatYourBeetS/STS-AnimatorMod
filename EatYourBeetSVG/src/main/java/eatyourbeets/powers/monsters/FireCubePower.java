package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameUtilities;

public class FireCubePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(FireCubePower.class);

    public FireCubePower(AbstractCreature owner, int value)
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
            GameActions.Bottom.GainStrength(amount);
            GameActions.Bottom.StackPower(null, new BurningPower(owner, null, amount))
            .ShowEffect(false, true)
            .IgnoreArtifact(true);
        }
        else for (AbstractCreature m : GameUtilities.GetAllEnemies(true))
        {
            GameActions.Bottom.StackPower(null, new StrengthPower(m, amount));
            GameActions.Bottom.StackPower(null, new BurningPower(m, null, amount))
            .ShowEffect(false, true)
            .IgnoreArtifact(true);
        }
    }
}
