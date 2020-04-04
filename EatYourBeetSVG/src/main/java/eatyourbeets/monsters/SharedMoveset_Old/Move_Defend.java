package eatyourbeets.monsters.SharedMoveset_Old;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_Defend extends EYBAbstractMove
{
    private final int blockAmount;

    public Move_Defend(int blockAmount)
    {
        this.blockAmount = blockAmount + CalculateAscensionBonus(blockAmount, 0.2f);
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.DEFEND);
    }

    public void QueueActions(AbstractCreature target)
    {
        GameActions.Bottom.GainBlock(owner, blockAmount);
    }
}
