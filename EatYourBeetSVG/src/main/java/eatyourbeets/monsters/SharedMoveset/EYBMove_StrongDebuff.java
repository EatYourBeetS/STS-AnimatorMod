package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.TargetHelper;

public class EYBMove_StrongDebuff extends EYBAbstractMove
{
    public EYBMove_StrongDebuff(PowerHelper power, int debuff)
    {
        this(debuff);

        AddPower(power);
    }

    public EYBMove_StrongDebuff(int debuff)
    {
        SetIntent(AbstractMonster.Intent.STRONG_DEBUFF);
        SetPowerTarget(TargetHelper.Player(owner));
        SetMisc(debuff);
    }
}
