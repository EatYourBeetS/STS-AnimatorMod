package eatyourbeets.powers.deprecated;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;

public class VitalityPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(VitalityPower.class.getSimpleName());

    public VitalityPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        this.amount = amount;

        updateDescription();
    }

    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != this.owner)
        {
            this.flash();

            TempHPField.tempHp.set(owner, TempHPField.tempHp.get(owner) + this.amount);
        }

        return damageAmount;
    }

    public void atStartOfTurn()
    {
        RemovePower();
    }
}
