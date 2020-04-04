package eatyourbeets.monsters;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.SharedMoveset.EYBMove_Attack;

public class EYBSharedMoveset
{
    public EYBAbstractMove Attack(int damage)
    {
        return new EYBMove_Attack()
        .SetIntent(AbstractMonster.Intent.ATTACK)
        .SetDamage(damage);
    }

    public EYBAbstractMove Attack(int damage, int times)
    {
        return new EYBMove_Attack()
        .SetIntent(AbstractMonster.Intent.ATTACK)
        .SetDamage(damage, times);
    }
}
