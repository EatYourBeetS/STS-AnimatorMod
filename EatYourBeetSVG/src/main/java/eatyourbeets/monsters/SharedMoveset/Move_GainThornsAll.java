package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.utilities.GameUtilities;

public class Move_GainThornsAll extends AbstractMove
{
    private final int buffAmount;

    public Move_GainThornsAll(int buffAmount)
    {
        this.buffAmount = buffAmount;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.BUFF);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        for (AbstractMonster m : GameUtilities.GetAllEnemies(true))
        {
            GameActions.Bottom.StackPower(owner, new ThornsPower(m, buffAmount));
        }
    }
}
