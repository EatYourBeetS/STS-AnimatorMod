package eatyourbeets.monsters.SharedMoveset.special;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.monsters.SharedMoveset.EYBMove_Buff;
import eatyourbeets.utilities.GameActions;

public class EYBMove_Buff_StrengthAndArtifact extends EYBMove_Buff
{
    private final int artifact;

    public EYBMove_Buff_StrengthAndArtifact(int strength, final int artifact)
    {
        super(strength);

        this.artifact = artifact;
    }

    public void QueueActions(AbstractCreature target)
    {
        GameActions.Bottom.StackPower(new StrengthPower(owner, misc.Calculate()));
        GameActions.Bottom.StackPower(new ArtifactPower(owner, artifact));
    }
}
