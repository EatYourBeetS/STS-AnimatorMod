package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameUtilities;

public class FrostWispPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(FrostWispPower.class.getSimpleName());

    public FrostWispPower(AbstractCreature owner, int value)
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
            GameActionsHelper_Legacy.ApplyPower(null, c, new PlatedArmorPower(c, amount), amount);
        }
    }
}
