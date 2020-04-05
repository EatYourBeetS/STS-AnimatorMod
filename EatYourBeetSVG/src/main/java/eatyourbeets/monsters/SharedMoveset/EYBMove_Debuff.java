package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.PowerTarget;

public class EYBMove_Debuff extends EYBAbstractMove
{
    public EYBMove_Debuff(PowerHelper power, int debuff)
    {
        this(debuff);

        AddPower(power);
    }

    public EYBMove_Debuff(int debuff)
    {
        SetMisc(debuff);

        intent = AbstractMonster.Intent.DEBUFF;
        powerTarget = PowerTarget.Player;
    }
}
