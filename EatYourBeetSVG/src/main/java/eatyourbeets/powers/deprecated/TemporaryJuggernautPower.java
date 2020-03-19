package eatyourbeets.powers.deprecated;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.JuggernautPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class TemporaryJuggernautPower extends JuggernautPower implements CloneablePowerInterface
{
    public TemporaryJuggernautPower(AbstractCreature owner, int amount)
    {
        super(owner, amount);

        this.ID = GR.Animator.CreateID(TemporaryJuggernautPower.class.getSimpleName());
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        GameActions.Bottom.RemovePower(owner, owner, this);
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new TemporaryJuggernautPower(owner, amount);
    }
}
