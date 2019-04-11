package eatyourbeets.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.GameActionsHelper;

public class FeridBathoryPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(FeridBathoryPower.class.getSimpleName());
    private int baseAmount;

    private static final int STRENGTH_AMOUNT = 1;
    private static final int HEAL_AMOUNT = 2;

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
        this.description = powerStrings.DESCRIPTIONS[0] + STRENGTH_AMOUNT + powerStrings.DESCRIPTIONS[1] + HEAL_AMOUNT + powerStrings.DESCRIPTIONS[2];
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
            GameActionsHelper.ApplyPower(owner, owner, new StrengthPower(owner, STRENGTH_AMOUNT), STRENGTH_AMOUNT);
            if (owner.currentHealth < owner.maxHealth)
            {
                GameActionsHelper.AddToBottom(new HealAction(owner, owner, HEAL_AMOUNT));
            }
            else
            {
                GameActionsHelper.GainTemporaryHP(owner, owner, HEAL_AMOUNT);
            }
            this.flash();
            this.amount -= 1;
        }
    }
}