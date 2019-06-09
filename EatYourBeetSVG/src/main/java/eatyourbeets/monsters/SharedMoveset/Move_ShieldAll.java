package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.powers.PlayerStatistics;

public class Move_ShieldAll extends AbstractMove
{
    private final int blockAmount;

    public Move_ShieldAll(int blockAmount)
    {
        this.blockAmount = blockAmount + ascensionLevel/6;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.DEFEND);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
        {
            GameActionsHelper.GainBlock(m, blockAmount);
        }
    }
}
