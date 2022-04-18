package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.replacement.AnimatorPlatedArmorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class FrostWispPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(FrostWispPower.class);

    public FrostWispPower(AbstractCreature owner, int amount)
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
            GameActions.Bottom.StackPower(null, new AnimatorPlatedArmorPower(c, amount));
        }
    }
}
