package pinacolada.powers.deprecated;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.JuggernautPower;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class TemporaryJuggernautPower extends JuggernautPower implements CloneablePowerInterface
{
    public TemporaryJuggernautPower(AbstractCreature owner, int amount)
    {
        super(owner, amount);

        this.ID = GR.PCL.CreateID(TemporaryJuggernautPower.class.getSimpleName());
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        PCLActions.Bottom.RemovePower(owner, owner, this);
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new TemporaryJuggernautPower(owner, amount);
    }
}
