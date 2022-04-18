package eatyourbeets.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class AnimatorMetallicizePower extends MetallicizePower implements CloneablePowerInterface
{
    public final int maxAmount;

    public AnimatorMetallicizePower(AbstractCreature owner, int amount)
    {
        super(owner, amount);

        this.maxAmount = (owner != null && owner.isPlayer && !Settings.isEndless) ? 30 : 999;

        if (this.amount > maxAmount)
        {
            this.amount = maxAmount;
        }
    }

    @Override
    public void updateDescription()
    {
        super.updateDescription();

        if (maxAmount > 0 && maxAmount < 999)
        {
            this.description += " NL " + GR.Animator.Strings.Misc.GainBlockAboveMaxStacks(maxAmount);
        }
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        if (amount > maxAmount)
        {
            final int block = (amount - maxAmount);
            if (block > 0)
            {
                GameActions.Bottom.GainBlock(block);
            }

            amount = maxAmount;
        }
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new AnimatorMetallicizePower(owner, amount);
    }
}
