package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.PowerTarget;

public class EYBMove_DefendDebuff extends EYBAbstractMove
{
    public EYBMove_DefendDebuff(int block, PowerHelper power, int debuff)
    {
        this(block, debuff);

        AddPower(power);
    }

    public EYBMove_DefendDebuff(int block, int debuff)
    {
        SetBlock(block);
        SetMisc(debuff);

        intent = AbstractMonster.Intent.DEFEND_DEBUFF;
        powerTarget = PowerTarget.Player;
    }
}
