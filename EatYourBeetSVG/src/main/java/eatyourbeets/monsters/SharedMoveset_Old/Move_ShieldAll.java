package eatyourbeets.monsters.SharedMoveset_Old;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.utilities.GameUtilities;

public class Move_ShieldAll extends EYBAbstractMove
{
    private final int blockAmount;

    public Move_ShieldAll(int blockAmount)
    {
        this.blockAmount = blockAmount + CalculateAscensionBonus(blockAmount, 0.12f);
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.DEFEND);
    }

    public void QueueActions(AbstractCreature target)
    {
        for (AbstractMonster m : GameUtilities.GetAllEnemies(true))
        {
            GameActions.Bottom.Add(new GainBlockAction(m, owner, blockAmount, true));
        }
    }
}
