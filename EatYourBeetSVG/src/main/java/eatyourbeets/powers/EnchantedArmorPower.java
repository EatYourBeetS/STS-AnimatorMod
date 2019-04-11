package eatyourbeets.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import java.text.DecimalFormat;

public class EnchantedArmorPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(EnchantedArmorPower.class.getSimpleName());

    private float percentage;

    public EnchantedArmorPower(AbstractPlayer owner, int resistance)
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
