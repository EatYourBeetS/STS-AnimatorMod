package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;

public class EYBMove_Attack extends EYBAbstractMove
{
    public EYBMove_Attack(int damage)
    {
        this(damage, 1);
    }

    public EYBMove_Attack(int damage, int times)
    {
        SetDamage(damage, times);

        intent = AbstractMonster.Intent.ATTACK;
    }
}
