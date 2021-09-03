package eatyourbeets.powers.animator;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.DelayedDamagePower;
import eatyourbeets.utilities.GameActions;

public class NegateBlockPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(NegateBlockPower.class);

    public NegateBlockPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        if (this.amount >= 9999)
        {
            this.amount = 9999;
        }
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;

        updateDescription();
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return 0f;
    }

    @Override
    public float modifyBlock(float blockAmount, AbstractCard card) {
        return 0f;
    }

    @Override
    public float modifyBlockLast(float blockAmount) {
        return 0f;
    }

    @Override
    public void onGainedBlock(float blockAmount) {
        int amount = MathUtils.floor(blockAmount);
        if (amount > 0)
            GameActions.Bottom.StackPower(new DelayedDamagePower(owner, amount));
    }

    @Override
    public int onPlayerGainedBlock(float blockAmount)
    {
        int amount = MathUtils.floor(blockAmount);
        if (amount > 0)
            GameActions.Bottom.StackPower(new DelayedDamagePower(owner, amount));
        return super.onPlayerGainedBlock(blockAmount);
    }

    @Override
    public int onPlayerGainedBlock(int blockAmount)
    {
        GameActions.Bottom.StackPower(new DelayedDamagePower(owner, amount));
        return super.onPlayerGainedBlock(blockAmount);
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();
        GameActions.Bottom.ReducePower(this, 1);
    }

}
