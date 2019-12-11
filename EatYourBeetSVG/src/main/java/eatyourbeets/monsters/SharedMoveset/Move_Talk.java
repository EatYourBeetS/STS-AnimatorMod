package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.AbstractMove;

public class Move_Talk extends AbstractMove
{
    private String line;

    public void SetMove()
    {
        this.owner.setMove(id, AbstractMonster.Intent.UNKNOWN);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        GameActions.Bottom.Talk(owner, line);
    }

    public void SetLine(String line)
    {
        this.line = line;
    }
}
