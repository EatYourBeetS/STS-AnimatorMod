package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class CounterAttackPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(CounterAttackPower.class);

    private static boolean removePower = true;

    public CounterAttackPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        if (this.amount >= 9999)
        {
            this.amount = 9999;
        }

        this.type = PowerType.BUFF;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        if (removePower)
        {
            this.description = powerStrings.DESCRIPTIONS[0] + (this.amount + getForceAgilityAmount()) + powerStrings.DESCRIPTIONS[2];
        }
        else
        {
            this.description = powerStrings.DESCRIPTIONS[1] + (this.amount + getForceAgilityAmount()) + powerStrings.DESCRIPTIONS[2];
        }
    }

    public static void setRemovePower(boolean value)
    {
        removePower = value;

        for (AbstractPower power : player.powers)
        {
            if (CounterAttackPower.POWER_ID.equals(power.ID))
            {
                power.updateDescription();
            }
        }
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount)
    {
        this.flashWithoutSound();

        if (info.owner instanceof AbstractMonster)
        {
            GameActions.Bottom.DealDamage(player, info.owner, amount + getForceAgilityAmount(), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        }

        if (removePower)
        {
            RemovePower();
        }

        return super.onAttackedToChangeDamage(info, damageAmount);
    }

    private int getForceAgilityAmount()
    {
        int amount = 0;

        int force = GameUtilities.GetPowerAmount(player, ForcePower.POWER_ID);
        if (force > 0)
        {
            amount += force;
        }

        int agility = GameUtilities.GetPowerAmount(player, AgilityPower.POWER_ID);
        if (agility > 0)
        {
            amount += agility;
        }

        return amount;
    }
}
