package eatyourbeets.powers.common;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ElectroPower;
import eatyourbeets.utilities.GameActions;

public class TemporaryElectroPower extends ElectroPower implements CloneablePowerInterface
{
    private boolean permanent;

    public TemporaryElectroPower(AbstractCreature owner)
    {
        super(owner);

        permanent = false;

        this.ID = ElectroPower.POWER_ID;
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
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        if (!permanent)
        {
            GameActions.Bottom.RemovePower(owner, owner, this);
        }
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new TemporaryElectroPower(owner);
    }
}
