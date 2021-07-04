package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.actions.basic.GainBlock;
import eatyourbeets.actions.basic.RemoveBlock;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class NegateBlockPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(NegateBlockPower.class);

    private int lastBlock;

    public NegateBlockPower(AbstractCreature owner, int amount, int lastBlock)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        if (this.amount >= 9999)
        {
            this.amount = 9999;
        }
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
        this.lastBlock = lastBlock;

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
        this.resetBlock();
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();
        this.lastBlock = Math.min(this.lastBlock, this.owner.currentBlock);
        GameActions.Bottom.ReducePower(this, 1);
    }

    private void resetBlock(){
        if (this.owner.currentBlock > 0) {
            this.lastBlock = Math.min(this.lastBlock, this.owner.currentBlock);
            if (this.lastBlock > 0) {
                GameActions.Bottom.Add(new RemoveBlock(this.owner, this.owner)).SetVFX(true, true);
                GameActions.Bottom.Add(new GainBlock(this.owner, this.owner, this.lastBlock, true)).SetVFX(true, true);
            }
            else {
                GameActions.Bottom.Add(new RemoveBlock(this.owner, this.owner)).SetVFX(true, false);
            }
        }
    }

}
