package eatyourbeets.powers.animatorClassic;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorClassicPower;
import eatyourbeets.utilities.GameActions;

public class FlamingWeaponPower extends AnimatorClassicPower
{
    public static final String POWER_ID = CreateFullID(FlamingWeaponPower.class);

    public FlamingWeaponPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }

    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        if (damageAmount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL)
        {
            this.flash();

            GameActions.Bottom.Add(new ApplyPowerAction(target, owner, new BurningPower(target, owner, amount), this.amount, true));
        }
    }
}