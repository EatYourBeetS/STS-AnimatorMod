package eatyourbeets.monsters.SharedMoveset_Old;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class Move_GainStrengthAndBlockAll extends EYBAbstractMove
{
    private final int strength;
    private final int block;

    public Move_GainStrengthAndBlockAll(int strength, int block)
    {
        this.strength = strength;
        this.block = block;
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.DEFEND_BUFF);
    }

    public void QueueActions(AbstractCreature target)
    {
        ArrayList<AbstractMonster> enemies = GameUtilities.GetAllEnemies(true);

        boolean isFast = enemies.size() >= 6;

        for (AbstractMonster m : enemies)
        {
            GameActions.Bottom.StackPower(owner, new StrengthPower(m, strength));
            GameActions.Bottom.Add(new GainBlockAction(m, owner, block, isFast));
        }
    }
}
