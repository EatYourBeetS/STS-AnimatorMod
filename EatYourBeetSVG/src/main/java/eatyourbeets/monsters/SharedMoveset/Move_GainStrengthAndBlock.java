package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.monsters.AbstractMove;

public class Move_GainStrengthAndBlock extends AbstractMove
{
    private final int strength;
    private final int block;

    public Move_GainStrengthAndBlock(int strength, int block)
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
        GameActions.Bottom.StackPower(new StrengthPower(owner, strength));
        GameActions.Bottom.Add(new GainBlockAction(owner, owner, block, true));
    }
}
