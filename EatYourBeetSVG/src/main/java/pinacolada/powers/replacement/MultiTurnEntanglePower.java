package pinacolada.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EntanglePower;
import pinacolada.utilities.PCLActions;

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
    public void atEndOfTurn(boolean isPlayer)
    {
        PCLActions.Bottom.ReducePower(this, 1);
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new MultiTurnEntanglePower(owner, amount);
    }
}
