package eatyourbeets.powers.animator;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class EarthenThornsPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(EarthenThornsPower.class);

    public EarthenThornsPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != this.owner)
        {
            GameActions.Top.DealDamage(owner, info.owner, amount, DamageInfo.DamageType.THORNS, AttackEffects.SLASH_HORIZONTAL)
            .SetVFX(true, false);
            this.flash();
        }

        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        RemovePower();
    }
}
