package eatyourbeets.monsters.Bosses.TheUnnamedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.powers.PoisonPlayerPower;

public class Move_Poison extends Move
{
    private int POISON_AMOUNT;

    public Move_Poison(int id, int ascensionLevel, TheUnnamed owner)
    {
        super((byte) id, ascensionLevel, owner);

        POISON_AMOUNT = 4;
    }

    @Override
    public boolean CanUse(Byte previousMove)
    {
        return false;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.STRONG_DEBUFF);
    }

    public void Execute(AbstractPlayer target)
    {
        GameActionsHelper.ApplyPower(owner, target, new PoisonPlayerPower(target, owner, POISON_AMOUNT), POISON_AMOUNT);
    }
}