package eatyourbeets.powers.common;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ElectroPower;
import eatyourbeets.utilities.GameActionsHelper;

public class TemporaryElectroPower extends ElectroPower implements CloneablePowerInterface
{
    private boolean permanent;

    public TemporaryElectroPower(AbstractCreature owner)
    {
        super(owner);

        permanent = false;
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        if (target == owner && (power instanceof ElectroPower && !(power instanceof TemporaryElectroPower)))
        {
            permanent = true;
        }
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        if (!permanent)
        {
            GameActionsHelper.AddToBottom(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new TemporaryElectroPower(owner);
    }
}
