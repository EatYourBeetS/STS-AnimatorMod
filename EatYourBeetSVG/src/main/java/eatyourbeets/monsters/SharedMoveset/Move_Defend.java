package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.AbstractMove;

public class Move_Defend extends AbstractMove
{
    private final int blockAmount;

    public Move_Defend(int blockAmount)
    {
        this.blockAmount = blockAmount + CalculateAscensionBonus(blockAmount, 0.2f);
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.DEFEND);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        GameActions.Bottom.GainBlock(owner, blockAmount);
    }
}
