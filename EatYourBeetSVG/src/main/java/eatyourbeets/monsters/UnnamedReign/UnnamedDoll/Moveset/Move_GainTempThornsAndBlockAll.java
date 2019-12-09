package eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.utilities.GameUtilities;

public class Move_GainTempThornsAndBlockAll extends AbstractMove
{
    private final int thorns;
    private final int block;

    public Move_GainTempThornsAndBlockAll(int thorns, int block)
    {
        this.thorns = thorns;
        this.block = block;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.DEFEND_BUFF);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        for (AbstractMonster m : GameUtilities.GetCurrentEnemies(true))
        {
            GameActionsHelper_Legacy.ApplyPower(owner, m, new EarthenThornsPower(m, thorns), thorns);
            GameActionsHelper_Legacy.GainBlock(m, block);
        }
    }
}
