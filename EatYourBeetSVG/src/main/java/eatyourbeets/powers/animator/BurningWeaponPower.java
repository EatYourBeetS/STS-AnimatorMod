package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.interfaces.subscribers.OnDamageOverrideSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.FreezingPower;
import eatyourbeets.utilities.GameActions;

public class BurningWeaponPower extends AnimatorPower implements OnDamageOverrideSubscriber
{
    public static final String POWER_ID = CreateFullID(BurningWeaponPower.class);
    public static final int DEFAULT_VALUE = 2;
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
        description = FormatDescription(0, amount, secondaryAmount);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        CombatStats.onDamageOverride.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onDamageOverride.Unsubscribe(this);
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
            GameActions.Top.ApplyBurning(owner, target, secondaryAmount).ShowEffect(true, true);
            if (target.hasPower(FreezingPower.POWER_ID)) {
                GameActions.Bottom.RemovePower(target, target, FreezingPower.POWER_ID);
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