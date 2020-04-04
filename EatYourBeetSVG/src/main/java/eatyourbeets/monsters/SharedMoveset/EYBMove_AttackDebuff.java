package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;

public class EYBMove_AttackDebuff extends EYBAbstractMove
{
    public EYBMove_AttackDebuff(int damage, int debuff)
    {
        this(damage, 1, debuff);
    }

    public EYBMove_AttackDebuff(int damage, int times, int debuff)
    {
        SetDamage(damage, times);
        SetMisc(debuff);

        intent = AbstractMonster.Intent.ATTACK_DEBUFF;
    }
}
