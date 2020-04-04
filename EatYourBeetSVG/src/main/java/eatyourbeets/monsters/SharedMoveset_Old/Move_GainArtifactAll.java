package eatyourbeets.monsters.SharedMoveset_Old;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.utilities.GameUtilities;

public class Move_GainArtifactAll extends EYBAbstractMove
{
    private final int buffAmount;

    public Move_GainArtifactAll(int buffAmount)
    {
        this.buffAmount = buffAmount;
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.BUFF);
    }

    public void QueueActions(AbstractCreature target)
    {
        for (AbstractMonster m : GameUtilities.GetAllEnemies(true))
        {
            GameActions.Bottom.StackPower(owner, new ArtifactPower(m, buffAmount));
        }
    }
}
