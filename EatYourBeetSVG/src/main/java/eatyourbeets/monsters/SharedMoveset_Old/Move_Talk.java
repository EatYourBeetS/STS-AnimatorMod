package eatyourbeets.monsters.SharedMoveset_Old;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_Talk extends EYBAbstractMove
{
    private String line;

    public Move_Talk()
    {
        this.intent = AbstractMonster.Intent.UNKNOWN;
    }

    public Move_Talk SetLine(String line)
    {
        this.line = line;

        return this;
    }

    public void QueueActions(AbstractCreature target)
    {
        GameActions.Bottom.Talk(owner, line);
    }
}
