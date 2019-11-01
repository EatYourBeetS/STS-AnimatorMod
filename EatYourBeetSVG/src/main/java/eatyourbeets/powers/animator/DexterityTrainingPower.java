package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.DexterityPower;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActionsHelper;

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

        GameActionsHelper.ApplyPower(owner, owner, new DexterityPower(owner, 1), 1);
        GameActionsHelper.AddToBottom(new ReducePowerAction(owner, owner, this, 1));
    }
}
