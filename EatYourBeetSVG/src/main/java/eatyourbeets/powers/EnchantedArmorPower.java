package eatyourbeets.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.text.DecimalFormat;

public class EnchantedArmorPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(EnchantedArmorPower.class.getSimpleName());

    private float percentage;

    public EnchantedArmorPower(AbstractCreature owner, int resistance)
    {
        super(owner, POWER_ID);
        this.amount = resistance;

        UpdatePercentage();

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        DecimalFormat df = new DecimalFormat("#.0");
        String value = df.format(((1 - this.percentage) * 100));

        this.description = powerStrings.DESCRIPTIONS[0] + value + powerStrings.DESCRIPTIONS[1];

        this.description += " NL NL Example: NL ";
        this.description += GetExampleDamage(5) + " NL ";
        this.description += GetExampleDamage(10) + " NL ";
        this.description += GetExampleDamage(20);
    }

    private String GetExampleDamage(int value)
    {
        return value + " -> " + "#g" + (int)(value * percentage);
    }

    @Override
    public void stackPower(int stackAmount)
    {
        this.amount += stackAmount;
        UpdatePercentage();
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType)
    {
        return damage * percentage;
    }

    private void UpdatePercentage()
    {
        percentage = 100f / (100f + this.amount);
    }
}
