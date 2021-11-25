package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class FlamingWeaponPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(FlamingWeaponPower.class);
    public int secondaryAmount;

    public FlamingWeaponPower(AbstractCreature owner, int amount) {
        this(owner, amount, amount);
    }

    public FlamingWeaponPower(AbstractCreature owner, int amount, int secondaryAmount)
    {
        super(owner, POWER_ID);
        this.secondaryAmount = secondaryAmount;

        Initialize(amount, PowerType.BUFF, true);
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount, secondaryAmount);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        super.onAttack(info, damageAmount, target);

        if (damageAmount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL)
        {
            GameActions.Top.ApplyBurning(owner, target, secondaryAmount).ShowEffect(true, true);
            this.flash();
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        ReducePower(1);
    }
}