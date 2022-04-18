package eatyourbeets.powers.unnamed;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.UnnamedPower;
import eatyourbeets.utilities.Mathf;

public class WitheringPower extends UnnamedPower
{
    public static final String POWER_ID = CreateFullID(WitheringPower.class);
    public static final int PERCENT_STACK_LOSS = 25;
    public static final int MAX_AMOUNT = 60;

    public static float ModifyDamage(float damage, int stacks)
    {
        return Mathf.Max(0, damage - damage * stacks * 0.01f);
    }

    public WitheringPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.maxAmount = MAX_AMOUNT;

        Initialize(amount, PowerType.DEBUFF, false);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, PERCENT_STACK_LOSS);
    }

    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.POWER_CONSTRICTED, 0.7f, 0.8f);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        return super.atDamageGive(type == DamageInfo.DamageType.NORMAL ? ModifyDamage(damage, amount) : damage, type);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        final int reduction = Mathf.RoundToInt(amount * PERCENT_STACK_LOSS * 0.01f);
        if (reduction > 0)
        {
            ReducePower(reduction);
        }
    }
}
