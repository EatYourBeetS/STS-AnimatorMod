package eatyourbeets.monsters.Bosses.KrulTepesMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.monsters.AbstractMove;

public class Move_PowerUp extends AbstractMove
{
    private final int ARTIFACT_AMOUNT;
    private final int STRENGTH_AMOUNT;

    public Move_PowerUp()
    {
        if (ascensionLevel >= 8)
        {
            STRENGTH_AMOUNT = 4;
            ARTIFACT_AMOUNT = 1;
        }
        else
        {
            STRENGTH_AMOUNT = 3;
            ARTIFACT_AMOUNT = 1;
        }
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.BUFF);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        GameActionsHelper_Legacy.ApplyPower(owner, owner, new ArtifactPower(owner, ARTIFACT_AMOUNT), ARTIFACT_AMOUNT);
        GameActionsHelper_Legacy.ApplyPower(owner, owner, new StrengthPower(owner, STRENGTH_AMOUNT), STRENGTH_AMOUNT);
    }
}