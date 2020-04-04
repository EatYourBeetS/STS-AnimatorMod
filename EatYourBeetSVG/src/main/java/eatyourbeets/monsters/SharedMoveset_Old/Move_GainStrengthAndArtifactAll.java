package eatyourbeets.monsters.SharedMoveset_Old;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.utilities.GameUtilities;

public class Move_GainStrengthAndArtifactAll extends EYBAbstractMove
{
    private final int strength;
    private final int artifact;

    public Move_GainStrengthAndArtifactAll(int strength, int artifact)
    {
        this.strength = strength;
        this.artifact = artifact;
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.BUFF);
    }

    public void QueueActions(AbstractCreature target)
    {
        for (AbstractMonster m : GameUtilities.GetAllEnemies(true))
        {
            GameActions.Bottom.StackPower(owner, new StrengthPower(m, strength));
            GameActions.Bottom.StackPower(owner, new ArtifactPower(m, artifact));
        }
    }
}
