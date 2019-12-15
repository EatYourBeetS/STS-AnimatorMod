package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class SonicPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(SonicPower.class.getSimpleName());

    public SonicPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.isTurnBased = true;

        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        GameActions.Bottom.GainDexterity(1);
        GameActions.Bottom.CreateThrowingKnives(1);

        GameActions.Bottom.ReducePower(this, 1);
    }
}