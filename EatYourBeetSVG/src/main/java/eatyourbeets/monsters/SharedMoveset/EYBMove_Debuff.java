package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.TargetHelper;

public class EYBMove_Debuff extends EYBAbstractMove
{
    public EYBMove_Debuff(PowerHelper power, int debuff)
    {
        this(debuff);

        AddPower(power);
    }

    public EYBMove_Debuff(int debuff)
    {
        SetIntent(AbstractMonster.Intent.DEBUFF);
        SetPowerTarget(TargetHelper.Player());
        SetMisc(debuff);
    }
}
