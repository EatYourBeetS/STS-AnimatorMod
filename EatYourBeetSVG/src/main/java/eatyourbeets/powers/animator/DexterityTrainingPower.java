package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class DexterityTrainingPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(DexterityTrainingPower.class.getSimpleName());

    public DexterityTrainingPower(AbstractPlayer owner, int duration)
    {
        super(owner, POWER_ID);

        this.amount = duration;
        this.isTurnBased = true;

        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        GameActions.Bottom.GainDexterity(1);
        GameActions.Bottom.ReducePower(this, 1);
    }
}
