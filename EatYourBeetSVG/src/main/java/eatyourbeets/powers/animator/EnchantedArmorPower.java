package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.interfaces.subscribers.OnRawDamageReceived;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;

import java.text.DecimalFormat;

public class EnchantedArmorPower extends AnimatorPower implements OnRawDamageReceived
{
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.0");

    public static final String POWER_ID = CreateFullID(EnchantedArmorPower.class);

    public final boolean reactive;
    public int attacksReceived;

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

        if (!reactive)
        {
            CombatStats.onRawDamageReceived.Subscribe(this);
        }
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onRawDamageReceived.Unsubscribe(this);
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
        if (type == DamageInfo.DamageType.NORMAL)
        {
            damage *= CalculatePercentage(reactive ? (amount + (int) damage) : amount);
        }

        return super.atDamageReceive(damage, type);
    }

    @Override
    public int OnRawDamageReceived(AbstractCreature target, DamageInfo info, int damage)
    {
        if (!reactive && target == owner && info.type == DamageInfo.DamageType.NORMAL)
        {
            attacksReceived += 1;
        }

        return damage;
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount)
    {
        if (reactive && info.type == DamageInfo.DamageType.NORMAL && info.owner != null)
        {
            IncreasePower(damageAmount);
        }

        return super.onAttackedToChangeDamage(info, damageAmount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        if (!reactive && attacksReceived > 0)
        {
            ReducePower(Math.min(10, attacksReceived));
            attacksReceived = 0;
        }
    }

    private String GetExampleDamage(int value)
    {
        return value + " -> " + "#g" + (int) (value * CalculatePercentage(amount));
    }
}
