package eatyourbeets.monsters.Bosses.KrulTepesMoveset;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_Cripple extends EYBAbstractMove
{
    private final int FRAIL_AMOUNT;
    private final int WEAK_AMOUNT;
    private final int BLOCK_AMOUNT;
    private final int VULNERABLE_AMOUNT;

    public Move_Cripple()
    {
        if (ascensionLevel >= 8)
        {
            FRAIL_AMOUNT = 2;
            VULNERABLE_AMOUNT = 2;
            WEAK_AMOUNT = 3;
            BLOCK_AMOUNT = 19;
        }
        else
        {
            FRAIL_AMOUNT = 2;
            VULNERABLE_AMOUNT = 2;
            WEAK_AMOUNT = 2;
            BLOCK_AMOUNT = 16;
        }
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.DEFEND_DEBUFF);
    }

    public void QueueActions(AbstractCreature target)
    {
        GameActions.Bottom.GainBlock(owner, BLOCK_AMOUNT);
        GameActions.Bottom.ApplyWeak(owner, target, WEAK_AMOUNT);
        GameActions.Bottom.ApplyVulnerable(owner, target, VULNERABLE_AMOUNT);
        GameActions.Bottom.ApplyFrail(owner, target, FRAIL_AMOUNT);
    }
}