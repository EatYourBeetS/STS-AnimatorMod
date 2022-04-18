package eatyourbeets.powers.deprecated;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public class MarkedPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(MarkedPower.class);

    public MarkedPower(AbstractCreature owner, int amount)
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
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType)
    {
        if (damageType == DamageInfo.DamageType.NORMAL)
        {
            damage += amount;
        }

        return super.atDamageReceive(damage, damageType);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.type == DamageInfo.DamageType.NORMAL)
        {
            GameActions.Bottom.RemovePower(owner, owner, this);
        }

        return super.onAttacked(info, damageAmount);
    }
}