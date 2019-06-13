package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActionsHelper;
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
        GameActionsHelper.ApplyPower(owner, owner, new StrengthPower(owner, strength), strength);
        GameActionsHelper.GainBlock(owner, block, true);
    }
}
