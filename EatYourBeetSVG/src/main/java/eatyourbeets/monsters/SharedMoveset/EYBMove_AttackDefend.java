package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;

public class EYBMove_AttackDefend extends EYBAbstractMove
{
    public EYBMove_AttackDefend(int damage, int block)
    {
        this(damage, 1, block);
    }

    public EYBMove_AttackDefend(int damage, int times, int block)
    {
        SetIntent(AbstractMonster.Intent.ATTACK_DEFEND);
        SetDamage(damage, times);
        SetBlock(block);
    }
}
