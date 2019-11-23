package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class Move_GainStrengthAndBlockAll extends AbstractMove
{
    private final int strength;
    private final int block;

    public Move_GainStrengthAndBlockAll(int strength, int block)
    {
        this.strength = strength;
        this.block = block;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.DEFEND_BUFF);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        ArrayList<AbstractMonster> enemies = GameUtilities.GetCurrentEnemies(true);

        boolean isFast = enemies.size() >= 6;

        for (AbstractMonster m : enemies)
        {
            GameActionsHelper.ApplyPower(owner, m, new StrengthPower(m, strength), strength, isFast);
            GameActionsHelper.GainBlock(m, block, isFast);
        }
    }
}
