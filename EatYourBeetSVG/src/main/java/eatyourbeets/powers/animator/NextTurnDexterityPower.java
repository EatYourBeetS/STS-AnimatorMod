package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActionsHelper;

public class NextTurnDexterityPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(NextTurnDexterityPower.class.getSimpleName());

    public NextTurnDexterityPower(AbstractCreature owner, int value)
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
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        GameActionsHelper.ApplyTemporaryDexterity(owner, owner, amount);
        LosePower();
    }
}
