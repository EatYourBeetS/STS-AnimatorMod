package eatyourbeets.monsters.Bosses.TheUnnamedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions._legacy.common.TheUnnamed_SummonDollAction;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.monsters.Bosses.TheUnnamed;

public class Move_SummonDoll extends AbstractMove
{
    private int uses = 3;
    private TheUnnamed theUnnamed;

    @Override
    public void Init(byte id, AbstractMonster owner)
    {
        super.Init(id, owner);

        theUnnamed = (TheUnnamed) owner;
    }

    @Override
    public boolean CanUse(Byte previousMove)
    {
        return uses > 0;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.UNKNOWN);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        uses -= 1;
        GameActionsHelper.AddToBottom(new TheUnnamed_SummonDollAction(theUnnamed));
    }
}
