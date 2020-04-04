package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;

public class EYBMove_StrongDebuff extends EYBAbstractMove
{
    public EYBMove_StrongDebuff(int debuff)
    {
        SetMisc(debuff);

        intent = AbstractMonster.Intent.STRONG_DEBUFF;
    }
}
