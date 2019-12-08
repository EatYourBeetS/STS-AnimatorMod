package eatyourbeets.powers.unnamed;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.UnnamedPower;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;

public class ResonancePower extends UnnamedPower
{
    public static final String POWER_ID = CreateFullID(ResonancePower.class.getSimpleName());

    public ResonancePower(AbstractCreature owner, int value)
    {
        super(owner, POWER_ID);

        this.amount = Math.min(999, value);

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount)
    {
        if (stackAmount > 0 && (amount + stackAmount) > 999)
        {
            stackAmount = 999 - amount;
        }

        super.stackPower(stackAmount);
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + amount + desc[1];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        GameActionsHelper.AddToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        return type == DamageInfo.DamageType.NORMAL ? damage + (float) this.amount : damage;
    }
}
