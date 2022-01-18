package pinacolada.powers.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.interfaces.subscribers.OnDamageOverrideSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.powers.common.FreezingPower;
import pinacolada.utilities.PCLActions;

public class BurningWeaponPower extends PCLPower implements OnDamageOverrideSubscriber
{
    public static final String POWER_ID = CreateFullID(BurningWeaponPower.class);
    public static final int DEFAULT_VALUE = 2;
    public static final int REMOVE_AMOUNT = 4;
    public int secondaryAmount = 2;

    public BurningWeaponPower(AbstractCreature owner, int amount) {
        this(owner, amount, DEFAULT_VALUE);
    }

    public BurningWeaponPower(AbstractCreature owner, int amount, int secondaryAmount)
    {
        super(owner, POWER_ID);
        this.secondaryAmount = secondaryAmount;

        Initialize(amount, PowerType.BUFF, true);
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount, secondaryAmount, REMOVE_AMOUNT);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        PCLCombatStats.onDamageOverride.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        PCLCombatStats.onDamageOverride.Unsubscribe(this);
    }

    @Override
    public float OnDamageOverride(AbstractCreature target, DamageInfo.DamageType type, float damage, AbstractCard card)
    {
        if (target.hasPower(FreezingPower.POWER_ID)) {
            return damage * 2;
        }
        return damage;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        super.onAttack(info, damageAmount, target);

        if (damageAmount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL)
        {
            PCLActions.Top.ApplyBurning(owner, target, secondaryAmount).ShowEffect(true, true);
            if (target.hasPower(FreezingPower.POWER_ID)) {
                PCLActions.Bottom.ReducePower(target, target, FreezingPower.POWER_ID, REMOVE_AMOUNT);
            }
            this.flash();
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        ReducePower(1);
    }
}