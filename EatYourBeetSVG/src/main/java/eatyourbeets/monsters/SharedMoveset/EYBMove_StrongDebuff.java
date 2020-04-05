package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.PowerTarget;

public class EYBMove_StrongDebuff extends EYBAbstractMove
{
    public EYBMove_StrongDebuff(PowerHelper power, int debuff)
    {
        this(debuff);

        AddPower(power);
    }

    public EYBMove_StrongDebuff(int debuff)
    {
        SetMisc(debuff);

        intent = AbstractMonster.Intent.STRONG_DEBUFF;
        powerTarget = PowerTarget.Player;
    }
}
