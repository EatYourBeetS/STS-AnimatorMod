package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public class MarkedPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(MarkedPower.class.getSimpleName());

    public MarkedPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.type = PowerType.DEBUFF;

        updateDescription();
    }

    public void playApplyPowerSfx()
    {
        CardCrawlGame.sound.playA("ORB_SLOT_GAIN", 1);
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