package eatyourbeets.powers.common;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.utilities.GameUtilities;

public class TemporaryArtifactPower extends ArtifactPower implements CloneablePowerInterface
{
    private int permanent;
    private int temporary;

    public static ApplyPowerAction Apply(AbstractCreature owner, AbstractCreature source, int amount)
    {
        ArtifactPower artifact = GameUtilities.GetPower(owner, ArtifactPower.POWER_ID);
        if (artifact != null && !(artifact instanceof TemporaryArtifactPower))
        {
            GameActions.Bottom.Add(new RemoveSpecificPowerAction(owner, source, artifact));

            return GameActions.Bottom.ApplyPower(source, owner, new TemporaryArtifactPower(owner, 1, artifact.amount), 1);
        }
        else
        {
            return GameActions.Bottom.ApplyPower(source, owner, new TemporaryArtifactPower(source, 1, 0), 1);
        }
    }

    public TemporaryArtifactPower(AbstractCreature owner, int temporary, int permanent)
    {
        super(owner, temporary + permanent);

        this.temporary = temporary;
        this.permanent = permanent;
    }

    @Override
    public void stackPower(int stackAmount)
    {
        // done on onApplyPower()
    }

    @Override
    public void reducePower(int reduceAmount)
    {
        super.reducePower(reduceAmount);

        if (amount > 0)
        {
            if (reduceAmount > temporary)
            {
                reduceAmount -= temporary;
                temporary = 0;
                permanent -= reduceAmount;
            }
            else
            {
                temporary -= reduceAmount;
            }
        }
        else
        {
            permanent = 0;
            temporary = 0;
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        if (target == owner && power instanceof ArtifactPower)
        {
            if (power instanceof TemporaryArtifactPower)
            {
                temporary += power.amount;
            }
            else
            {
                permanent += power.amount;
            }

            super.stackPower(power.amount);
        }
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        if (temporary > 0)
        {
            GameActionsHelper_Legacy.AddToBottom(new ReducePowerAction(owner, owner, this, temporary));
        }
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new TemporaryArtifactPower(owner, temporary, permanent);
    }
}
