package eatyourbeets.monsters.SharedMoveset_Old;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_GainStrengthAndArtifact extends EYBAbstractMove
{
    private final int strength;
    private final int artifact;

    public Move_GainStrengthAndArtifact(int strength, int artifact)
    {
        this.strength = strength;
        this.artifact = artifact;
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.DEFEND_BUFF);
    }

    public void QueueActions(AbstractCreature target)
    {
        GameActions.Bottom.StackPower(new StrengthPower(owner, strength));
        GameActions.Bottom.StackPower(new ArtifactPower(owner, artifact));
    }
}
