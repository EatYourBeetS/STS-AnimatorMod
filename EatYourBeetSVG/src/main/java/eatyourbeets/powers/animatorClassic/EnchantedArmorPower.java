package eatyourbeets.powers.animatorClassic;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;

import java.text.DecimalFormat;

public class EnchantedArmorPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(EnchantedArmorPower.class);

    public final boolean reactive;

    private float percentage;

    public static float CalculatePercentage(int amount)
    {
        return 100f / (100f + amount);
    }

    public EnchantedArmorPower(AbstractCreature owner, int resistance, boolean reactive)
    {
        super(owner, POWER_ID);

        this.amount = resistance;
        this.reactive = reactive;

        UpdatePercentage();
        updateDescription();
    }

    public EnchantedArmorPower(AbstractCreature owner, int resistance)
    {
        this(owner, resistance, false);
    }

    @Override
    public void updateDescription()
    {
        if (amount > 0)
        {
            DecimalFormat df = new DecimalFormat("#.0");
            String value = df.format(((1 - this.percentage) * 100));

            this.description = powerStrings.DESCRIPTIONS[0] + value + powerStrings.DESCRIPTIONS[1];

            if (!reactive)
            {
                this.description += " NL NL Example: NL ";
                this.description += GetExampleDamage(5) + " NL ";
                this.description += GetExampleDamage(10) + " NL ";
                this.description += GetExampleDamage(20);
            }
        }
        else
        {
            this.description = powerStrings.DESCRIPTIONS[0] + "0" + powerStrings.DESCRIPTIONS[1];
        }
    }

    private String GetExampleDamage(int value)
    {
        return value + " -> " + "#g" + (int) (value * percentage);
    }

    @Override
    public void stackPower(int stackAmount)
    {
        this.amount += stackAmount;
        UpdatePercentage();
        updateDescription();
    }

    @Override
    public void reducePower(int reduceAmount)
    {
        this.amount -= reduceAmount;
        UpdatePercentage();
        updateDescription();
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type)
    {
        return calculateDamageReceived(damage, 0);
    }

    public float calculateDamageReceived(float damage, float modifier)
    {
        if (reactive)
        {
            return damage * (CalculatePercentage(this.amount + (int) damage) + modifier);
        }
        else
        {
            return damage * (percentage + modifier);
        }
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount)
    {
        if (reactive)
        {
            if (info.type == DamageInfo.DamageType.HP_LOSS || info.type == DamageInfo.DamageType.THORNS)
            {
                float percentage = CalculatePercentage(this.amount + (damageAmount / 2));

                damageAmount = (int) Math.ceil((float) damageAmount * percentage);
                info.output = damageAmount;
            }
            else if (info.owner != null)
            {
                stackPower(damageAmount);
            }
        }
        else
        {
            if (info.type == DamageInfo.DamageType.THORNS || info.type == DamageInfo.DamageType.HP_LOSS)
            {
                damageAmount = Math.round(percentage * (float) damageAmount);
                info.output = damageAmount;
            }
        }

        return super.onAttackedToChangeDamage(info, damageAmount);
    }

    private void UpdatePercentage()
    {
        percentage = CalculatePercentage(this.amount);
    }
}
