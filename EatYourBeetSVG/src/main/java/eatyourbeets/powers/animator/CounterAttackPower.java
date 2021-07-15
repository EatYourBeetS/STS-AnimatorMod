package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.affinity.AgilityPower;
import eatyourbeets.powers.affinity.ForcePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class CounterAttackPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(CounterAttackPower.class);

    private int baseAmount;

    private static boolean removePower = true;

    public CounterAttackPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.baseAmount = amount;

        this.type = PowerType.BUFF;

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount)
    {
        this.baseAmount += stackAmount;
        this.updateDescription();
    }

    @Override
    public void atStartOfTurn()
    {
        updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.amount = baseAmount + getForceAgilityAmount();

        if (removePower)
        {
            this.description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[2];
        }
        else
        {
            this.description = powerStrings.DESCRIPTIONS[1] + amount + powerStrings.DESCRIPTIONS[2];
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
    public void onRemove()
    {
        super.onRemove();

        this.baseAmount = 0;
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount)
    {
        this.flashWithoutSound();

        if (info.owner instanceof AbstractMonster)
        {
            GameActions.Bottom.DealDamage(player, info.owner, amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HEAVY);
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
