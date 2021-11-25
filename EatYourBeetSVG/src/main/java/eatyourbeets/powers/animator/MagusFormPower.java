package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class MagusFormPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(MagusFormPower.class);

    public int secondaryAmount = -1;

    public MagusFormPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    public void atStartOfTurn()
    {
        super.atStartOfTurn();
        GameActions.Bottom.GainFocus(amount);
        GameActions.Bottom.GainStrength(secondaryAmount);
        GameActions.Bottom.GainDexterity(secondaryAmount);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, secondaryAmount);
    }
}
