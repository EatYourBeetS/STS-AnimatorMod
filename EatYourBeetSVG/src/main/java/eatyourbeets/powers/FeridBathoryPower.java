package eatyourbeets.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class FeridBathoryPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(FeridBathoryPower.class.getSimpleName());
    private int baseAmount;

    public FeridBathoryPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.baseAmount = amount;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0] + "1" + powerStrings.DESCRIPTIONS[1] + "2" + powerStrings.DESCRIPTIONS[2];
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
        baseAmount += stackAmount;
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        this.amount = baseAmount;
    }

    @Override
    public void onExhaust(AbstractCard card)
    {
        super.onExhaust(card);

        if (this.amount > 0)
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new StrengthPower(owner, 1), 1));
            AbstractDungeon.actionManager.addToBottom(new HealAction(owner, owner, 2));

            this.flash();
            this.amount -= 1;
        }
    }
}