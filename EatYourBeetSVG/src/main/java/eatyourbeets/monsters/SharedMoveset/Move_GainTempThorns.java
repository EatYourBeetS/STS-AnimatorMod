package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.powers.EarthenThornsPower;

public class Move_GainTempThorns extends AbstractMove
{
    private final int amount;

    public Move_GainTempThorns(int amount)
    {
        this.amount = amount;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.BUFF);
    }

    public void Execute(AbstractPlayer target)
    {
        GameActionsHelper.ApplyPower(owner, owner, new EarthenThornsPower(owner, amount), amount);
    }
}
