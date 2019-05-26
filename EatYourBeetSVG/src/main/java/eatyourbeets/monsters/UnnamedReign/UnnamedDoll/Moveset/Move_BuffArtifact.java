package eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.TheUnnamed_Doll;
import eatyourbeets.powers.PlayerStatistics;

public class Move_BuffArtifact extends Move
{
    private final int BUFF_AMOUNT;

    public Move_BuffArtifact(int id, int ascensionLevel, TheUnnamed_Doll owner, TheUnnamed theUnnamed)
    {
        super((byte) id, ascensionLevel, owner, theUnnamed);

        BUFF_AMOUNT = 1;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.BUFF);
    }

    public void Execute(AbstractPlayer target)
    {
        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
        {
            GameActionsHelper.ApplyPower(owner, m, new ArtifactPower(m, BUFF_AMOUNT), BUFF_AMOUNT);
        }
    }
}
