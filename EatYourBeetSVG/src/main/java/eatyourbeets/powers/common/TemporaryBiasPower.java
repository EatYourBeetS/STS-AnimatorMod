package eatyourbeets.powers.common;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BiasPower;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class TemporaryBiasPower extends BiasPower implements CloneablePowerInterface
{
    public TemporaryBiasPower(AbstractCreature owner, int amount)
    {
        super(owner, amount);

        this.ID = AnimatorPower.CreateFullID(TemporaryBiasPower.class.getSimpleName());
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        GameActions.Bottom.RemovePower(owner, owner, this);
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new TemporaryBiasPower(owner, amount);
    }
}
