package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.JuggernautPower;
import eatyourbeets.utilities.GameActionsHelper;

public class TemporaryJuggernautPower extends JuggernautPower
{
    public TemporaryJuggernautPower(AbstractPlayer owner, int amount)
    {
        super(owner, amount);

        this.ID = AnimatorPower.CreateFullID(TemporaryJuggernautPower.class.getSimpleName());
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        GameActionsHelper.AddToBottom(new RemoveSpecificPowerAction(owner, owner, this));
    }
}
