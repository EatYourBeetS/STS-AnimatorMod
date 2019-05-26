package eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.TheUnnamed_Doll;

public abstract class Move extends AbstractMove
{
    protected final TheUnnamed theUnnamed;
    protected final TheUnnamed_Doll theUnnamed_Minion;

    public Move(byte id, int ascensionLevel, AbstractMonster owner, TheUnnamed theUnnamed)
    {
        super(id, ascensionLevel, owner);
        theUnnamed_Minion = (TheUnnamed_Doll) owner;
        this.theUnnamed = theUnnamed;
    }
}
