package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.CommonPower;

public class TaintedPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(TaintedPower.class);

    public TaintedPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount, PowerType.DEBUFF, false);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        return super.atDamageGive(type == DamageInfo.DamageType.NORMAL ? (damage - GetDamageDecrease()) : damage, type);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType)
    {
        return super.atDamageReceive(damage += amount, damageType);
    }

    @Override
    public void atEndOfRound()
    {
        super.atEndOfRound();

        ReducePower(Math.max(1,amount / 2));
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, GetDamageDecrease());
    }

    public int GetDamageDecrease() {
        return 1 + amount / 10;
    }
}