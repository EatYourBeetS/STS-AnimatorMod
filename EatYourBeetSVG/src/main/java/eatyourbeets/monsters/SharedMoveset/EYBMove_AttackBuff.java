package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.PowerTarget;

public class EYBMove_AttackBuff extends EYBAbstractMove
{
    public EYBMove_AttackBuff(int damage, PowerHelper power, int buff)
    {
        this(damage, buff);

        AddPower(power);
    }

    public EYBMove_AttackBuff(int damage, int times, PowerHelper power, int buff)
    {
        this(damage, times, buff);

        AddPower(power);
    }

    public EYBMove_AttackBuff(int damage, int buff)
    {
        this(damage, 1, buff);
    }

    public EYBMove_AttackBuff(int damage, int times, int buff)
    {
        SetMisc(buff);
        SetDamage(damage, times);

        intent = AbstractMonster.Intent.ATTACK_BUFF;
        powerTarget = PowerTarget.Source;
    }
}
