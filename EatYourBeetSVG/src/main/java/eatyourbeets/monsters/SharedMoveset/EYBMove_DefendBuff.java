package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.PowerTarget;

public class EYBMove_DefendBuff extends EYBAbstractMove
{
    public EYBMove_DefendBuff(int block, PowerHelper power, int buff)
    {
        this(block, buff);

        AddPower(power);
    }

    public EYBMove_DefendBuff(int block, int buff)
    {
        SetBlock(block);
        SetMisc(buff);

        intent = AbstractMonster.Intent.DEFEND_BUFF;
        powerTarget = PowerTarget.Source;
    }
}
