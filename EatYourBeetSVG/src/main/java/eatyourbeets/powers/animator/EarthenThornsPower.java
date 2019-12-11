package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class EarthenThornsPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(EarthenThornsPower.class.getSimpleName());

    public EarthenThornsPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        this.amount = amount;

        updateDescription();
    }

    public void stackPower(int stackAmount)
    {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        this.updateDescription();
    }

    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != this.owner)
        {
            this.flash();

            GameActions.Top.DealDamage(owner, info.owner, amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
            .SetOptions2(true, false, 0);
        }

        return damageAmount;
    }

    public void atStartOfTurn()
    {
        LosePower();
    }
}
