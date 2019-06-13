package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;

public class Move_GainStrength extends AbstractMove
{
    private final int amount;

    public Move_GainStrength(int amount)
    {
        this.amount = amount;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.BUFF);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        GameActionsHelper.ApplyPower(owner, owner, new StrengthPower(owner, amount), amount);
    }
}
