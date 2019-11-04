package eatyourbeets.powers.common;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.ElectroPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;

public class TemporaryArtifactPower extends ArtifactPower implements CloneablePowerInterface
{
    private int permanent;
    private int temporary;

    public static void Apply(AbstractCreature owner, AbstractCreature source, int amount)
    {
        ArtifactPower artifact = Utilities.GetPower(owner, ArtifactPower.POWER_ID);
        if (artifact != null && !(artifact instanceof TemporaryArtifactPower))
        {
            GameActionsHelper.AddToBottom(new RemoveSpecificPowerAction(owner, source, artifact));
            GameActionsHelper.ApplyPower(source, owner, new TemporaryArtifactPower(owner, 1, artifact.amount), 1);
        }
        else
        {
            GameActionsHelper.ApplyPower(source, owner, new TemporaryArtifactPower(source, 1, 0), 1);
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
            GameActionsHelper.AddToBottom(new ReducePowerAction(owner, owner, this, temporary));
        }
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new TemporaryArtifactPower(owner, temporary, permanent);
    }
}
