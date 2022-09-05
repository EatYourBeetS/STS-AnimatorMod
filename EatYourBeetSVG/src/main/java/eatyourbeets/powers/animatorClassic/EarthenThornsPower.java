package eatyourbeets.powers.animatorClassic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorClassicPower;
import eatyourbeets.utilities.GameActions;

public class EarthenThornsPower extends AnimatorClassicPower
{
    public static final String POWER_ID = CreateFullID(EarthenThornsPower.class);

    public EarthenThornsPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        this.amount = amount;

        updateDescription();
    }

    public void stackPower(int stackAmount)
    {
        this.fontScale = 8f;
        this.amount += stackAmount;
        this.updateDescription();
    }

    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != this.owner)
        {
            this.flash();

            GameActions.Top.DealDamage(owner, info.owner, amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
            .SetVFX(true, false);
        }

        return damageAmount;
    }

    public void atStartOfTurn()
    {
        RemovePower();
    }
}
