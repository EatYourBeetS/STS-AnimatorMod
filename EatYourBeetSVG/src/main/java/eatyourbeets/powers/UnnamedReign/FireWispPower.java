package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameUtilities;

public class FireWispPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(FireWispPower.class.getSimpleName());

    public FireWispPower(AbstractCreature owner, int value)
    {
        super(owner, POWER_ID);

        this.amount = value;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + amount + desc[1];
    }

    @Override
    public void onDeath()
    {
        super.onDeath();

        for (AbstractCreature c : GameUtilities.GetAllCharacters(true))
        {
            GameActionsHelper.ApplyPowerSilently(null, c, new BurningPower(c, null, amount), amount);
            GameActionsHelper.ApplyPower(null, c, new StrengthPower(c, amount), amount);
        }
    }
}