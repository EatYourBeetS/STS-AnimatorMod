package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.utilities.GameUtilities;

public class Move_ShieldAll extends AbstractMove
{
    private final int blockAmount;

    public Move_ShieldAll(int blockAmount)
    {
        this.blockAmount = blockAmount + GetBonus(blockAmount, 0.12f);
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.DEFEND);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        for (AbstractMonster m : GameUtilities.GetCurrentEnemies(true))
        {
            GameActions.Bottom.Add(new GainBlockAction(m, owner, blockAmount, true));
        }
    }
}
