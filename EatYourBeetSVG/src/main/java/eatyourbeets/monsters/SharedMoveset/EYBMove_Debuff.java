package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;

public class EYBMove_Debuff extends EYBAbstractMove
{
    public EYBMove_Debuff(int debuff)
    {
        SetMisc(debuff);

        intent = AbstractMonster.Intent.DEBUFF;
    }
}
