package eatyourbeets.monsters.Bosses.KrulTepesMoveset;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.utilities.GameUtilities;

public class Move_Regenerate extends EYBAbstractMove
{
    private final int REGEN_AMOUNT;
    private final int BLOCK_AMOUNT;
    private final int THORNS_AMOUNT;

    public Move_Regenerate()
    {
        if (ascensionLevel >= 8)
        {
            REGEN_AMOUNT = 6;
            BLOCK_AMOUNT = 20;
            THORNS_AMOUNT = 1;
        }
        else
        {
            REGEN_AMOUNT = 5;
            BLOCK_AMOUNT = 18;
            THORNS_AMOUNT = 1;
        }
    }

    @Override
    public boolean CanUse(Byte previousMove)
    {
        return super.CanUse(previousMove) && GameUtilities.GetPowerAmount(owner, RegenPower.POWER_ID) < 20;
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.DEFEND_BUFF);
    }

    public void QueueActions(AbstractCreature target)
    {
        GameActions.Bottom.GainBlock(owner, BLOCK_AMOUNT);
        GameActions.Bottom.StackPower(new RegenPower(owner, REGEN_AMOUNT));
        GameActions.Bottom.StackPower(new ThornsPower(owner, THORNS_AMOUNT));
    }
}