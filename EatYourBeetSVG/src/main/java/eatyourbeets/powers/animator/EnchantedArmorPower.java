package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.interfaces.subscribers.OnModifyDamageSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;

import java.text.DecimalFormat;

public class EnchantedArmorPower extends AnimatorPower implements OnModifyDamageSubscriber
{
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.0");

    public static final String POWER_ID = CreateFullID(EnchantedArmorPower.class);

    public final boolean reactive;
    public int damageTaken;

    public static float CalculatePercentage(int amount)
    {
        return 100f / (100f + amount);
    }

    public EnchantedArmorPower(AbstractCreature owner, int resistance)
    {
        this(owner, resistance, false);
    }

    public EnchantedArmorPower(AbstractCreature owner, int resistance, boolean reactive)
    {
        super(owner, POWER_ID);

        this.reactive = reactive;

        Initialize(resistance);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        CombatStats.onModifyDamage.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onModifyDamage.Unsubscribe(this);
    }

    @Override
    public void updateDescription()
    {
        if (amount > 0)
        {
            this.description = FormatDescription(0, decimalFormat.format(((1 - CalculatePercentage(amount)) * 100)));

            if (!reactive)
            {
                this.description += " NL NL Example: NL ";
                this.description += GetExampleDamage(amount + 5) + " NL ";
                this.description += GetExampleDamage(amount + 10) + " NL ";
                this.description += GetExampleDamage(amount + 20);
            }
        }
        else
        {
            this.description = FormatDescription(0, 0);
        }
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type)
    {
        if (reactive)
        {
            damage *= CalculatePercentage(amount + (int) damage);
        }
        else if (type == DamageInfo.DamageType.NORMAL)
        {
            damage *= CalculatePercentage(amount);
        }

        return super.atDamageReceive(damage, type);
    }

    @Override
    public int OnModifyDamage(AbstractCreature target, DamageInfo info, int damage)
    {
        if (!reactive && info.type == DamageInfo.DamageType.NORMAL)
        {
            damageTaken += damage;
        }

        return damage;
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
                updateDescription();
            }
        }
//        else
//        {
//            if (info.type == DamageInfo.DamageType.THORNS || info.type == DamageInfo.DamageType.HP_LOSS)
//            {
//                damageAmount = Math.round(percentage * (float) damageAmount);
//                info.output = damageAmount;
//            }
//        }

        return super.onAttackedToChangeDamage(info, damageAmount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        if (!reactive && damageTaken > 0)
        {
            ReducePower(damageTaken / 5);
            damageTaken = 0;
        }
    }

    private String GetExampleDamage(int value)
    {
        return value + " -> " + "#g" + (int) (value * CalculatePercentage(amount));
    }
}
