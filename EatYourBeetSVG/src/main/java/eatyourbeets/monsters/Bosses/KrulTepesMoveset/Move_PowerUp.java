package eatyourbeets.monsters.Bosses.KrulTepesMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.utilities.GameUtilities;

public class Move_PowerUp extends AbstractMove
{
    private final int ARTIFACT_AMOUNT;
    private final int STRENGTH_AMOUNT;

    public Move_PowerUp()
    {
        STRENGTH_AMOUNT = 3;
        ARTIFACT_AMOUNT = 1;
    }

    @Override
    public boolean CanUse(Byte previousMove)
    {
        return super.CanUse(previousMove) && GameUtilities.GetPowerAmount(owner, StrengthPower.POWER_ID) <= 9;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.BUFF);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        GameActions.Bottom.StackPower(new ArtifactPower(owner, ARTIFACT_AMOUNT));
        GameActions.Bottom.StackPower(new StrengthPower(owner, STRENGTH_AMOUNT));
    }
}