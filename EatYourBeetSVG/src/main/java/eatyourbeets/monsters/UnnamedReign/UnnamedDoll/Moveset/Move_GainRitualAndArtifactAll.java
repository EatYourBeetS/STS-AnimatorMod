package eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.RitualPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.utilities.GameUtilities;

public class Move_GainRitualAndArtifactAll extends EYBAbstractMove
{
    private final int ritual;
    private final int artifact;

    public Move_GainRitualAndArtifactAll(int ritual, int artifact)
    {
        this.ritual = ritual;
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
            GameActions.Bottom.ApplyPower(owner, m, new RitualPower(m, ritual, false), ritual);
            GameActions.Bottom.ApplyPower(owner, m, new ArtifactPower(m, artifact), artifact);
        }
    }
}