package eatyourbeets.monsters.Bosses.KrulTepesMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.monsters.AbstractMove;

public class Move_Cripple extends AbstractMove
{
    private final int FRAIL_AMOUNT;
    private final int WEAK_AMOUNT;
    private final int BLOCK_AMOUNT;
    private final int VULNERABLE_AMOUNT;

    public Move_Cripple()
    {
        if (ascensionLevel >= 8)
        {
            FRAIL_AMOUNT = 2;
            WEAK_AMOUNT = 3;
            VULNERABLE_AMOUNT = 3;
            BLOCK_AMOUNT = 19;
        }
        else
        {
            FRAIL_AMOUNT = 2;
            WEAK_AMOUNT = 2;
            VULNERABLE_AMOUNT = 2;
            BLOCK_AMOUNT = 16;
        }
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.DEFEND_DEBUFF);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        GameActionsHelper.GainBlock(owner, BLOCK_AMOUNT);
        GameActionsHelper.ApplyPower(owner, target, new WeakPower(target, WEAK_AMOUNT, true), WEAK_AMOUNT);
        GameActionsHelper.ApplyPower(owner, target, new VulnerablePower(target, VULNERABLE_AMOUNT, true), VULNERABLE_AMOUNT);
        GameActionsHelper.ApplyPower(owner, target, new FrailPower(target, FRAIL_AMOUNT, true), FRAIL_AMOUNT);
    }
}