package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;

public class EYBMove_Buff extends EYBAbstractMove
{
    public EYBMove_Buff(int buff)
    {
        SetMisc(buff);

        intent = AbstractMonster.Intent.BUFF;
    }
}
