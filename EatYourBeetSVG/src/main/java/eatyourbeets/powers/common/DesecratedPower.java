package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.interfaces.subscribers.OnDamageOverrideSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;

public class DesecratedPower extends CommonPower implements OnDamageOverrideSubscriber
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

        CombatStats.onDamageOverride.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onDamageOverride.Unsubscribe(this);
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
}