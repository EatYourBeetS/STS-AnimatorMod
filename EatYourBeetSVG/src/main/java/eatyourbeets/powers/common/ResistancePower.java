package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.powers.replacement.AnimatorFrailPower;
import eatyourbeets.powers.replacement.AnimatorVulnerablePower;
import eatyourbeets.powers.replacement.AnimatorWeakPower;

public class ResistancePower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(ResistancePower.class);
    public static final int MULTIPLIER = 5;
    public static final int MULTIPLIER2 = 3;
    public static final int DEFENSE_MULTIPLIER = 4;
    private int totalMultiplier = 0;
    private int totalMultiplier2 = 0;

    public static float CalculatePercentage(int amount)
    {
        return Math.max(0.1f, 1f - amount * DEFENSE_MULTIPLIER / 100f);
    }

    public ResistancePower(AbstractCreature owner, int amount)
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
        this.description = FormatDescription(amount >= 0 ? 0 : 1, totalMultiplier, totalMultiplier2, amount * DEFENSE_MULTIPLIER);
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
    public float atDamageReceive(float damage, DamageInfo.DamageType type)
    {
        if (type == DamageInfo.DamageType.NORMAL)
        {
            damage *= CalculatePercentage(amount);
        }

        return super.atDamageReceive(damage, type);
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
        AnimatorVulnerablePower.AddPlayerModifier(this.totalMultiplier);
        AnimatorWeakPower.AddPlayerModifier(this.totalMultiplier2);
        AnimatorFrailPower.AddPlayerModifier(this.totalMultiplier2);

        this.totalMultiplier = MULTIPLIER * this.amount;
        this.totalMultiplier2 = MULTIPLIER2 * this.amount;

        AnimatorVulnerablePower.AddPlayerModifier(-this.totalMultiplier);
        AnimatorWeakPower.AddPlayerModifier(-this.totalMultiplier2);
        AnimatorFrailPower.AddPlayerModifier(-this.totalMultiplier2);
    }
}
