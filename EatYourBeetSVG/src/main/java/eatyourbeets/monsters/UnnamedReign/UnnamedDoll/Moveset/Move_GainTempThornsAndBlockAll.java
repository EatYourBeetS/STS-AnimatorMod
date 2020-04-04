package eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.utilities.GameUtilities;

public class Move_GainTempThornsAndBlockAll extends EYBAbstractMove
{
    private final int thorns;
    private final int block;

    public Move_GainTempThornsAndBlockAll(int thorns, int block)
    {
        this.thorns = thorns;
        this.block = block;
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.DEFEND_BUFF);
    }

    public void QueueActions(AbstractCreature target)
    {
        for (AbstractMonster m : GameUtilities.GetAllEnemies(true))
        {
            GameActions.Bottom.ApplyPower(owner, m, new EarthenThornsPower(m, thorns), thorns);
            GameActions.Bottom.Add(new GainBlockAction(m, owner, block));
        }
    }
}
