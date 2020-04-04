package eatyourbeets.monsters.SharedMoveset_Old;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_GainArtifact extends EYBAbstractMove
{
    private final int buffAmount;

    public Move_GainArtifact(int buffAmount)
    {
        this.buffAmount = buffAmount;
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.BUFF);
    }

    public void QueueActions(AbstractCreature target)
    {
        GameActions.Bottom.ApplyPower(owner, owner, new ArtifactPower(owner, buffAmount), buffAmount);
    }
}
