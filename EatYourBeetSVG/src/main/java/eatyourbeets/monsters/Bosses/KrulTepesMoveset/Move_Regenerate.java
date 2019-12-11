package eatyourbeets.monsters.Bosses.KrulTepesMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.AbstractMove;

public class Move_Regenerate extends AbstractMove
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
        AbstractPower power = owner.getPower(RegenPower.POWER_ID);

        return power == null || power.amount < 20;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.DEFEND_BUFF);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        GameActions.Bottom.GainBlock(owner, BLOCK_AMOUNT);
        GameActions.Bottom.StackPower(new RegenPower(owner, REGEN_AMOUNT));
        GameActions.Bottom.StackPower(new ThornsPower(owner, THORNS_AMOUNT));
    }
}