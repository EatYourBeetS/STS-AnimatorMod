package eatyourbeets.monsters.Bosses.TheUnnamedMinionMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.monsters.Bosses.TheUnnamed_Minion;

public abstract class Move extends AbstractMove
{
    protected final TheUnnamed theUnnamed;
    protected final TheUnnamed_Minion theUnnamed_Minion;

    public Move(byte id, int ascensionLevel, AbstractMonster owner, TheUnnamed theUnnamed)
    {
        super(id, ascensionLevel, owner);
        theUnnamed_Minion = (TheUnnamed_Minion) owner;
        this.theUnnamed = theUnnamed;
    }
}
