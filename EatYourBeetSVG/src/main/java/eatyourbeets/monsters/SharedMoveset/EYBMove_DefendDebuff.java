package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;

public class EYBMove_DefendDebuff extends EYBAbstractMove
{
    public EYBMove_DefendDebuff(int block, int debuff)
    {
        SetBlock(block);
        SetMisc(debuff);

        intent = AbstractMonster.Intent.DEFEND_DEBUFF;
    }
}
