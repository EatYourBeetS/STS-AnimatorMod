package pinacolada.powers.deprecated;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BiasPower;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class TemporaryBiasPower extends BiasPower implements CloneablePowerInterface
{
    public TemporaryBiasPower(AbstractCreature owner, int amount)
    {
        super(owner, amount);

        this.ID = GR.PCL.CreateID(TemporaryBiasPower.class.getSimpleName());
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        PCLActions.Bottom.RemovePower(owner, owner, this);
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new TemporaryBiasPower(owner, amount);
    }
}
