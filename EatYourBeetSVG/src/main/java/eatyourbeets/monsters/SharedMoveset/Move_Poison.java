package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.powers.animator.PoisonPlayerPower;

public class Move_Poison extends AbstractMove
{
    public int poisonAmount;

    public Move_Poison(int amount)
    {
        poisonAmount = amount;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.STRONG_DEBUFF);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        GameActionsHelper.ApplyPower(owner, target, new PoisonPlayerPower(target, owner, poisonAmount), poisonAmount);
    }
}