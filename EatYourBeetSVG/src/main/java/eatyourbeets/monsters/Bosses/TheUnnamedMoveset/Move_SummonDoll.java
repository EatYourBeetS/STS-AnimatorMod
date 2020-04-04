package eatyourbeets.monsters.Bosses.TheUnnamedMoveset;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.actions.monsters.TheUnnamed_SummonDollAction;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.monsters.Bosses.TheUnnamed;

public class Move_SummonDoll extends EYBAbstractMove
{
    private int uses = 3;
    private TheUnnamed theUnnamed;

    @Override
    public void Initialize(byte id, AbstractMonster owner)
    {
        super.Initialize(id, owner);

        theUnnamed = (TheUnnamed) owner;
    }

    @Override
    public boolean CanUse(Byte previousMove)
    {
        return uses > 0;
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.UNKNOWN);
    }

    public void QueueActions(AbstractCreature target)
    {
        uses -= 1;
        GameActions.Bottom.Add(new TheUnnamed_SummonDollAction(theUnnamed));
    }
}
