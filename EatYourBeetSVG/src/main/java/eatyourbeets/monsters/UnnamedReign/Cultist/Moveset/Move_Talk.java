package eatyourbeets.monsters.UnnamedReign.Cultist.Moveset;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.monsters.UnnamedReign.Cultist.TheUnnamed_Cultist;

public class Move_Talk extends AbstractMove
{
    private String line;

    public Move_Talk(int id, int ascensionLevel, TheUnnamed_Cultist owner)
    {
        super((byte) id, ascensionLevel, owner);
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.UNKNOWN);
    }

    public void Execute(AbstractPlayer target)
    {
        GameActionsHelper.AddToBottom(new TalkAction(owner, line));
    }

    public void SetLine(String line)
    {
        this.line = line;
    }
}
