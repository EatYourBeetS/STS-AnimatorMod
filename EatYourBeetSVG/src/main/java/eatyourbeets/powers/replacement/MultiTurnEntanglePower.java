package eatyourbeets.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EntanglePower;
import eatyourbeets.utilities.GameActions;

public class MultiTurnEntanglePower extends EntanglePower implements CloneablePowerInterface
{
    public MultiTurnEntanglePower(AbstractCreature owner, int amount)
    {
        super(owner);

        this.ID = "MultiTurn" + ID;
        this.amount = amount;
    }

    @Override
    public void onInitialApplication()
    {
        for (int i = 0; i < owner.powers.size(); i++)
        {
            final AbstractPower p = owner.powers.get(i);
            if (p.ID.equals(EntanglePower.POWER_ID))
            {
                owner.powers.remove(i);
                amount += p.amount;
                break;
            }
        }

        this.ID = EntanglePower.POWER_ID;
    }

    @Override
    public void reducePower(int reduceAmount)
    {
        super.reducePower(reduceAmount);

        if (amount <= 0)
        {
            GameActions.Bottom.RemovePower(owner, this);
        }
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new MultiTurnEntanglePower(owner, amount);
    }
}
