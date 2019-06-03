package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;

public class Move_Defend extends AbstractMove
{
    private final int blockAmount;

    public Move_Defend(int blockAmount)
    {
        this.blockAmount = blockAmount + ascensionLevel/5;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.DEFEND);
    }

    public void Execute(AbstractPlayer target)
    {
        GameActionsHelper.GainBlock(owner, blockAmount);
    }
}
