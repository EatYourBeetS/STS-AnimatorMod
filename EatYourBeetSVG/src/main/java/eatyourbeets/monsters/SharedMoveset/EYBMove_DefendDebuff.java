package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.TargetHelper;

public class EYBMove_DefendDebuff extends EYBAbstractMove
{
    public EYBMove_DefendDebuff(int block, PowerHelper power, int debuff)
    {
        this(block, debuff);

        AddPower(power);
    }

    public EYBMove_DefendDebuff(int block, int debuff)
    {
        SetIntent(AbstractMonster.Intent.DEFEND_DEBUFF);
        SetPowerTarget(TargetHelper.Player(owner));
        SetBlock(block);
        SetMisc(debuff);
    }
}
