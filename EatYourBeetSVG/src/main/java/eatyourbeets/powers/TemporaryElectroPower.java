package eatyourbeets.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.ElectroPower;
import eatyourbeets.GameActionsHelper;

public class TemporaryElectroPower extends ElectroPower
{
    public TemporaryElectroPower(AbstractPlayer owner)
    {
        super(owner);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        GameActionsHelper.AddToBottom(new RemoveSpecificPowerAction(owner, owner, this));
    }
}
