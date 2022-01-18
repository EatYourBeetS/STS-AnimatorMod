package pinacolada.powers.common;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.powers.PCLPower;

import java.text.DecimalFormat;

public class EnchantedArmorPower extends PCLPower
{
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.0");

    public static final String POWER_ID = CreateFullID(EnchantedArmorPower.class);

    public static float CalculatePercentage(int amount)
    {
        return 100f / (100f + amount);
    }

    public EnchantedArmorPower(AbstractCreature owner, int resistance)
    {
        super(owner, POWER_ID);

        Initialize(resistance);
    }

    @Override
    public void updateDescription()
    {
        if (amount > 0)
        {
            this.description = FormatDescription(0, decimalFormat.format(((1 - CalculatePercentage(amount)) * 100)));
        }
        else
        {
            this.description = FormatDescription(0, 0);
        }
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type)
    {
        if (type == DamageInfo.DamageType.NORMAL)
        {
            damage *= CalculatePercentage(amount + (int) damage);
        }

        return super.atDamageReceive(damage, type);
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount)
    {
        if (info.type == DamageInfo.DamageType.NORMAL && info.owner != null)
        {
            IncreasePower(damageAmount);
        }

        return super.onAttackedToChangeDamage(info, damageAmount);
    }

    private String GetExampleDamage(int value)
    {
        return value + " -> " + "#g" + (int) (value * CalculatePercentage(amount));
    }
}
