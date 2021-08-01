package eatyourbeets.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EnvenomPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class TemporaryEnvenomPower extends EnvenomPower implements CloneablePowerInterface
{
    public TemporaryEnvenomPower(AbstractCreature owner, int amount)
    {
        super(owner, amount);

        this.ID = GR.Animator.CreateID(TemporaryEnvenomPower.class.getSimpleName());
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
        return new TemporaryEnvenomPower(owner, amount);
    }
}
