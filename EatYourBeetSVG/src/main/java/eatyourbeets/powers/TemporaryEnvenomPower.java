package eatyourbeets.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.EnvenomPower;
import eatyourbeets.GameActionsHelper;

public class TemporaryEnvenomPower extends EnvenomPower
{
    public TemporaryEnvenomPower(AbstractPlayer owner, int amount)
    {
        super(owner, amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        GameActionsHelper.AddToBottom(new RemoveSpecificPowerAction(owner, owner, this));
    }
}
