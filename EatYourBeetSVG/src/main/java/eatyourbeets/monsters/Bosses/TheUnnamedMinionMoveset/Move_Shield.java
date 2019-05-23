package eatyourbeets.monsters.Bosses.TheUnnamedMinionMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.monsters.Bosses.TheUnnamed_Minion;
import eatyourbeets.powers.PlayerStatistics;

public class Move_Shield extends Move
{
    private final int BLOCK_AMOUNT;

    public Move_Shield(int id, int ascensionLevel, TheUnnamed_Minion owner, TheUnnamed theUnnamed)
    {
        super((byte) id, ascensionLevel, owner, theUnnamed);

        BLOCK_AMOUNT = 16;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.DEFEND);
    }

    public void Execute(AbstractPlayer target)
    {
        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
        {
            GameActionsHelper.GainBlock(m, BLOCK_AMOUNT);
        }
    }
}
