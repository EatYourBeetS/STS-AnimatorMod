package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.PowerTarget;

public class EYBMove_Buff extends EYBAbstractMove
{
    public EYBMove_Buff(PowerHelper power, int buff)
    {
        this(buff);

        AddPower(power);
    }

    public EYBMove_Buff(int buff)
    {
        SetMisc(buff);

        intent = AbstractMonster.Intent.BUFF;
        powerTarget = PowerTarget.Source;
    }
}
