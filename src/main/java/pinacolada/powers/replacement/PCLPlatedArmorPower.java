package pinacolada.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class PCLPlatedArmorPower extends PlatedArmorPower implements CloneablePowerInterface
{
    public final int maxAmount;

    public PCLPlatedArmorPower(AbstractCreature owner, int amount)
    {
        super(owner, amount);

        this.maxAmount = (owner != null && owner.isPlayer) ? 30 : 999;

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
            this.description += " NL " + GR.PCL.Strings.Misc.GainBlockAboveMaxStacks(maxAmount);
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
                PCLActions.Bottom.GainBlock(block);
            }

            amount = maxAmount;
        }
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new PCLPlatedArmorPower(owner, amount);
    }
}
