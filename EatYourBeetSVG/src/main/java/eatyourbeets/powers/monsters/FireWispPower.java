package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.GameUtilities;

public class FireWispPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(FireWispPower.class);

    public FireWispPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void onDeath()
    {
        super.onDeath();

        for (AbstractCreature c : GameUtilities.GetAllCharacters(true))
        {
            GameActions.Bottom.StackPower(null, new StrengthPower(c, amount));
            GameActions.Bottom.StackPower(null, new BurningPower(c, null, amount))
            .ShowEffect(false, true)
            .IgnoreArtifact(true);
        }
    }
}