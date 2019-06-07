package eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.powers.EarthenThornsPower;
import eatyourbeets.powers.PlayerStatistics;

public class Move_GainTempThornsAndRegenAll extends AbstractMove
{
    private final int thorns;
    private final int regen;

    public Move_GainTempThornsAndRegenAll(int thorns, int regen)
    {
        this.thorns = thorns;
        this.regen = regen;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.BUFF);
    }

    public void Execute(AbstractPlayer target)
    {
        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
        {
            GameActionsHelper.ApplyPower(owner, m, new EarthenThornsPower(m, thorns), thorns);
            GameActionsHelper.ApplyPower(owner, m, new RegenPower(m, regen), regen);
        }
    }
}
