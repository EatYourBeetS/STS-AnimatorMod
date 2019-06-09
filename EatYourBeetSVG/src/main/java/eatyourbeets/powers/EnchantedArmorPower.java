package eatyourbeets.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.text.DecimalFormat;

public class EnchantedArmorPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(EnchantedArmorPower.class.getSimpleName());

    public boolean reactive;

    private float percentage;

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
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type)
    {
        if (reactive)
        {
            return damage * CalculatePercentage(this.amount + (int) damage);
        }
        else
        {
            return damage * percentage;
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.type == DamageInfo.DamageType.THORNS || info.type == DamageInfo.DamageType.HP_LOSS)
        {
            damageAmount = Math.round(percentage * (float) damageAmount);
            info.output = damageAmount;
        }

        if (reactive && info.type != DamageInfo.DamageType.HP_LOSS)
        {
            stackPower(damageAmount);
        }

        return super.onAttacked(info, damageAmount);
    }

    private float CalculatePercentage(int amount)
    {
        return 100f / (100f + amount);
    }

    private void UpdatePercentage()
    {
        percentage = CalculatePercentage(this.amount);
    }
}
