package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.powers.ApplyPower;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class PestilencePower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(PestilencePower.class);
    protected static final int PESTILENCE_MODIFIER = 2;

    public PestilencePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        Initialize(amount);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount * PESTILENCE_MODIFIER);
        if (amount > 0)
        {
            this.type = PowerType.BUFF;
        }
        else {
            this.type = PowerType.DEBUFF;
        }
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
    }

    @Override
    public void reducePower(int reduceAmount)
    {
        super.reducePower(reduceAmount);
    }

    @Override
    public void onRemove()
    {
        this.amount = 0;
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        int amountToApply = this.amount * PESTILENCE_MODIFIER;

        if (GameUtilities.IsPlayer(source) && GameUtilities.IsCommonDebuff(power))
        {
            power.amount += amountToApply;

            final AbstractGameAction action = AbstractDungeon.actionManager.currentAction;
            if (action instanceof ApplyPower || action instanceof ApplyPowerAction)
            {
                action.amount += amountToApply;
            }
            else
            {
                JUtils.LogWarning(this, "Unknown action type: " + action.getClass().getName());
            }
        }
    }

}
