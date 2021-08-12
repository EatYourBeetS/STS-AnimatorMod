package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class FlamingWeaponPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(FlamingWeaponPower.class);

    public FlamingWeaponPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        super.onAttack(info, damageAmount, target);

        if (damageAmount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL)
        {
            GameActions.Top.ApplyBurning(owner, target, amount).ShowEffect(false, true);
            this.flash();
        }
    }
}