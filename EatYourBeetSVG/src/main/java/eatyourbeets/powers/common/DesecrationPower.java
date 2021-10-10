package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.powers.ApplyPower;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.powers.replacement.AnimatorFrailPower;
import eatyourbeets.powers.replacement.AnimatorLockOnPower;
import eatyourbeets.powers.replacement.AnimatorVulnerablePower;
import eatyourbeets.powers.replacement.AnimatorWeakPower;
import eatyourbeets.utilities.JUtils;

public class DesecrationPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(DesecrationPower.class);
    public static final int MULTIPLIER = 15;
    public static final int MULTIPLIER2 = 5;
    private int totalMultiplier;
    private int totalMultiplier2;

    public DesecrationPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        Initialize(amount);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        UpdatePercentage();
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, this.totalMultiplier,this.totalMultiplier2,this.amount);
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

        UpdatePercentage();
    }

    @Override
    public void reducePower(int reduceAmount)
    {
        super.reducePower(reduceAmount);
        UpdatePercentage();
    }

    @Override
    public void onRemove()
    {
        this.amount = 0;
        UpdatePercentage();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        if (power.ID.equals(DelayedDamagePower.POWER_ID))
        {
            power.amount += this.amount;

            final AbstractGameAction action = AbstractDungeon.actionManager.currentAction;
            if (action instanceof ApplyPower || action instanceof ApplyPowerAction)
            {
                action.amount += this.amount;
            }
            else
            {
                JUtils.LogWarning(this, "Unknown action type: " + action.getClass().getName());
            }
        }
    }

    @Override
    protected void onAmountChanged(int previousAmount, int difference)
    {
        super.onAmountChanged(previousAmount, difference);

        if (amount == 0) {
            RemovePower();
        }
    }

    public void UpdatePercentage()
    {
        //Undo the previous changes made by this power
        AnimatorLockOnPower.AddEnemyModifier(-this.totalMultiplier);
        AnimatorVulnerablePower.AddEnemyModifier(-this.totalMultiplier);
        AnimatorWeakPower.AddEnemyModifier(-this.totalMultiplier2);
        AnimatorFrailPower.AddEnemyModifier(-this.totalMultiplier2);

        this.totalMultiplier = MULTIPLIER * this.amount;
        this.totalMultiplier2 = MULTIPLIER2 * this.amount;

        AnimatorLockOnPower.AddEnemyModifier(this.totalMultiplier);
        AnimatorVulnerablePower.AddEnemyModifier(this.totalMultiplier);
        AnimatorWeakPower.AddEnemyModifier(this.totalMultiplier2);
        AnimatorFrailPower.AddEnemyModifier(this.totalMultiplier2);
    }
}
