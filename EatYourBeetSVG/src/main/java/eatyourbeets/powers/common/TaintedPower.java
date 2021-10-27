package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CommonPower;

public class TaintedPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(TaintedPower.class);
    public static final int DAMAGE_DECREASE = 1;
    public static final int ORB_MULTIPLIER = 10;

    public TaintedPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount, PowerType.DEBUFF, false);
    }

    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.ORB_SLOT_GAIN, 2.1f, 2.3f);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        return super.atDamageGive(type == DamageInfo.DamageType.NORMAL ? (damage - DAMAGE_DECREASE) : damage, type);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType)
    {
        if (damageType == DamageInfo.DamageType.NORMAL)
        {
            damage += amount;
        }
        return super.atDamageReceive(damage, damageType);
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
        this.description = FormatDescription(0, amount, ORB_MULTIPLIER, DAMAGE_DECREASE);
    }
}