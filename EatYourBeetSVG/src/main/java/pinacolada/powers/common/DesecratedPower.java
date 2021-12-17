package pinacolada.powers.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.interfaces.subscribers.OnDamageOverrideSubscriber;
import pinacolada.interfaces.subscribers.OnOrbApplyLockOnSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;

public class DesecratedPower extends PCLPower implements OnDamageOverrideSubscriber, OnOrbApplyLockOnSubscriber
{
    public static final String POWER_ID = CreateFullID(DesecratedPower.class);

    public static float GetDamageDealtDecrease(int amount) {
        return amount * 0.125f;
    }

    public static float GetDamageReceivedIncrease(int amount) {
        return amount * 0.25f;
    }

    public DesecratedPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount, PowerType.DEBUFF, false);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        PCLCombatStats.onOrbApplyLockOn.Subscribe(this);
        PCLCombatStats.onDamageOverride.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        PCLCombatStats.onOrbApplyLockOn.Unsubscribe(this);
        PCLCombatStats.onDamageOverride.Unsubscribe(this);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        return super.atDamageGive(type == DamageInfo.DamageType.NORMAL ? (damage * (1f - GetDamageDealtDecrease(amount))) : damage, type);
    }

    @Override
    public float OnDamageOverride(AbstractCreature target, DamageInfo.DamageType type, float damage, AbstractCard card)
    {
        if (target == owner) {
            return damage * (1f + GetDamageReceivedIncrease(amount));
        }

        return damage;
    }

    @Override
    public void atEndOfRound()
    {
        super.atEndOfRound();

        RemovePower();
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, GetDamageReceivedIncrease(amount) * 100, GetDamageDealtDecrease(amount) * 100);
    }

    @Override
    public int OnOrbApplyLockOn(int retVal, AbstractCreature target, int dmg) {
        if (target == owner) {
            return (int) (retVal * (1f + GetDamageReceivedIncrease(amount)));
        }

        return retVal;
    }
}