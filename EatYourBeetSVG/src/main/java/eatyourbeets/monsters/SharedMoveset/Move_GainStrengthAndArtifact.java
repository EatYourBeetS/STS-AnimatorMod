package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;

public class Move_GainStrengthAndArtifact extends AbstractMove
{
    private final int strength;
    private final int artifact;

    public Move_GainStrengthAndArtifact(int strength, int artifact)
    {
        this.strength = strength;
        this.artifact = artifact;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.DEFEND_BUFF);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        GameActionsHelper.ApplyPower(owner, owner, new StrengthPower(owner, strength), strength);
        GameActionsHelper.ApplyPower(owner, owner, new ArtifactPower(owner, artifact), artifact);
    }
}
