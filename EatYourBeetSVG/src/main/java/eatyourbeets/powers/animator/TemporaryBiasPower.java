package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.BiasPower;
import eatyourbeets.utilities.GameActionsHelper;

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
