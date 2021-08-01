package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class DarkWispPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(DarkWispPower.class);

    public DarkWispPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void onDeath()
    {
        super.onDeath();

        GameActions.Bottom.ApplyConstricted(TargetHelper.AllCharacters(null), amount);
    }
}
