package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.powers.PoisonAffinityPower;

public class Move_GainPoisonAffinity extends AbstractMove
{
    private final int amount;

    public Move_GainPoisonAffinity(int amount)
    {
        this.amount = amount;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.BUFF);
    }

    public void Execute(AbstractPlayer target)
    {
        GameActionsHelper.ApplyPower(owner, owner, new PoisonAffinityPower(owner, amount), amount);
    }
}
