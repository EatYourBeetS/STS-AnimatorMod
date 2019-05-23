package eatyourbeets.monsters.Bosses.TheUnnamedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.monsters.Bosses.TheUnnamed;

public abstract class Move extends AbstractMove
{
    protected final TheUnnamed theUnnamed;

    public Move(byte id, int ascensionLevel, AbstractMonster owner)
    {
        super(id, ascensionLevel, owner);
        theUnnamed = (TheUnnamed) owner;
    }
}
