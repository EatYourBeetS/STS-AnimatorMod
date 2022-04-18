package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;

public class EYBMove_Stunned extends EYBAbstractMove
{
    public EYBMove_Stunned()
    {
        SetIntent(AbstractMonster.Intent.STUN);
    }
}
