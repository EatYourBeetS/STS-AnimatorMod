package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.PowerTarget;

public class EYBMove_AttackDebuff extends EYBAbstractMove
{
    public EYBMove_AttackDebuff(int damage, PowerHelper power, int debuff)
    {
        this(damage, debuff);

        AddPower(power);
    }

    public EYBMove_AttackDebuff(int damage, int times, PowerHelper power, int debuff)
    {
        this(damage, times, debuff);

        AddPower(power);
    }

    public EYBMove_AttackDebuff(int damage, int debuff)
    {
        this(damage, 1, debuff);
    }

    public EYBMove_AttackDebuff(int damage, int times, int debuff)
    {
        SetDamage(damage, times);
        SetMisc(debuff);

        intent = AbstractMonster.Intent.ATTACK_DEBUFF;
        powerTarget = PowerTarget.Player;
    }
}
