package eatyourbeets.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.BiasPower;
import com.megacrit.cardcrawl.powers.ElectroPower;
import eatyourbeets.GameActionsHelper;

public class TemporaryBiasPower extends BiasPower
{
    public TemporaryBiasPower(AbstractPlayer owner, int amount)
    {
        super(owner, amount);

        this.ID = AnimatorPower.CreateFullID(TemporaryBiasPower.class.getSimpleName());
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        GameActionsHelper.AddToBottom(new RemoveSpecificPowerAction(owner, owner, this));
    }
}
