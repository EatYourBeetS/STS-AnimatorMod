package eatyourbeets.monsters.Bosses.TheUnnamedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.TheUnnamed_SummonMinionAction;
import eatyourbeets.monsters.Bosses.TheUnnamed;

public class Move_Summon extends Move
{
    public Move_Summon(int id, int ascensionLevel, TheUnnamed owner)
    {
        super((byte) id, ascensionLevel, owner);
    }

    @Override
    public boolean CanUse(Byte previousMove)
    {
        return false;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.UNKNOWN);
    }

    public void Execute(AbstractPlayer target)
    {
        GameActionsHelper.AddToBottom(new TheUnnamed_SummonMinionAction(theUnnamed));
    }
}
