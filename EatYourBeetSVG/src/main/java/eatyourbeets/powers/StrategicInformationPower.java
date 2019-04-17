package eatyourbeets.powers;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.RandomCostReductionAction;

public class StrategicInformationPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(StrategicInformationPower.class.getSimpleName());

    private static final int BLOCK_AMOUNT = 2;

    private int lastDiscardCount;
    private int uses;

    public StrategicInformationPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.uses = amount;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1] + BLOCK_AMOUNT + powerStrings.DESCRIPTIONS[2];
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        this.lastDiscardCount = GameActionManager.totalDiscardedThisTurn;
    }

    @Override
    public void stackPower(int stackAmount)
    {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        this.uses += stackAmount;
    }

    @Override
    public void onDrawOrDiscard()
    {
        super.onDrawOrDiscard();

        int discarded = GameActionManager.totalDiscardedThisTurn;
        if (amount > 0 && lastDiscardCount < discarded)
        {
            while (amount > 0 && lastDiscardCount < discarded)
            {
                GameActionsHelper.GainBlock(owner, BLOCK_AMOUNT);
                GameActionsHelper.AddToBottom(new RandomCostReductionAction(1, false));
                amount -= 1;
                lastDiscardCount += 1;
            }

            updateDescription();
        }
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        lastDiscardCount = 0;
        amount = uses;
    }
}