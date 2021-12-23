package pinacolada.powers.common;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;

public class ResistancePower extends PCLPower
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
        this.canGoNegative = true;
        this.maxAmount = 12;
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
        this.description = FormatDescription(amount >= 0 ? 0 : 1, Math.abs(amount * DEFENSE_MULTIPLIER), Math.abs(totalMultiplier), Math.abs(totalMultiplier2), maxAmount);
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
    protected void onAmountChanged(int previousAmount, int difference)
    {
        super.onAmountChanged(previousAmount, difference);

        if (amount == 0) {
            RemovePower();
        }
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

    public void UpdatePercentage()
    {
        //Undo the previous changes made by this power
        PCLCombatStats.AddPlayerEffectBonus(ImpairedPower.POWER_ID, this.totalMultiplier);
        PCLCombatStats.AddPlayerEffectBonus(VulnerablePower.POWER_ID, this.totalMultiplier);
        PCLCombatStats.AddPlayerEffectBonus(WeakPower.POWER_ID, this.totalMultiplier2);
        PCLCombatStats.AddPlayerEffectBonus(FrailPower.POWER_ID, this.totalMultiplier2);

        this.totalMultiplier = MULTIPLIER * this.amount;
        this.totalMultiplier2 = MULTIPLIER2 * this.amount;

        PCLCombatStats.AddPlayerEffectBonus(ImpairedPower.POWER_ID, -this.totalMultiplier);
        PCLCombatStats.AddPlayerEffectBonus(VulnerablePower.POWER_ID, -this.totalMultiplier);
        PCLCombatStats.AddPlayerEffectBonus(WeakPower.POWER_ID, -this.totalMultiplier2);
        PCLCombatStats.AddPlayerEffectBonus(FrailPower.POWER_ID, -this.totalMultiplier2);
    }
}
