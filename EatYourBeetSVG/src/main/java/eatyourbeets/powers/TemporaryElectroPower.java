package eatyourbeets.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ElectroPower;
import eatyourbeets.GameActionsHelper;

public class TemporaryElectroPower extends ElectroPower
{
    private boolean permanent;

    public TemporaryElectroPower(AbstractPlayer owner)
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
}
