package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;

public class EYBMove_DefendBuff extends EYBAbstractMove
{
    public EYBMove_DefendBuff(int block, int buff)
    {
        SetBlock(block);
        SetMisc(buff);

        intent = AbstractMonster.Intent.DEFEND_BUFF;
    }
}
