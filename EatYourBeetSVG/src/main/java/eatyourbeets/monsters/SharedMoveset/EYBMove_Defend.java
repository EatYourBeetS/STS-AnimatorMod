package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;

public class EYBMove_Defend extends EYBAbstractMove
{
    public EYBMove_Defend(int block)
    {
        SetBlock(block);

        intent = AbstractMonster.Intent.DEFEND;
    }
}
