package eatyourbeets.powers.common;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RetainCardPower;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class TemporaryRetainPower extends RetainCardPower implements CloneablePowerInterface
{
    public TemporaryRetainPower(AbstractCreature owner, int numCards)
    {
        super(owner, numCards);

        this.ID = AnimatorPower.CreateFullID(TemporaryRetainPower.class.getSimpleName());
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
        return new TemporaryRetainPower(owner, amount);
    }
}