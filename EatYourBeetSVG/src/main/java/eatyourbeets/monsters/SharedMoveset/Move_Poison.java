package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.powers.PoisonPlayerPower;

public class Move_Poison extends AbstractMove
{
    private final int poisonAmount;

    public Move_Poison(int amount)
    {
        poisonAmount = amount;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.STRONG_DEBUFF);
    }

    public void Execute(AbstractPlayer target)
    {
        GameActionsHelper.ApplyPower(owner, target, new PoisonPlayerPower(target, owner, poisonAmount), poisonAmount);
    }
}