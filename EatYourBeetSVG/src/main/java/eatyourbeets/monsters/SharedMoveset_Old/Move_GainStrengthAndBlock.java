package eatyourbeets.monsters.SharedMoveset_Old;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_GainStrengthAndBlock extends EYBAbstractMove
{
    private final int strength;
    private final int block;

    public Move_GainStrengthAndBlock(int strength, int block)
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
        GameActions.Bottom.StackPower(new StrengthPower(owner, strength));
        GameActions.Bottom.Add(new GainBlockAction(owner, owner, block, true));
    }
}
