package eatyourbeets.monsters.SharedMoveset.special;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.monsters.SummonMonsterAction;
import eatyourbeets.monsters.SharedMoveset.EYBMove_Unknown;
import eatyourbeets.utilities.GameActions;

public class EYBMove_SummonEnemy extends EYBMove_Unknown
{
    private AbstractMonster summon;

    @Override
    public void Initialize(byte id, AbstractMonster owner)
    {
        super.Initialize(id, owner);
    }

    public EYBMove_SummonEnemy SetSummon(AbstractMonster monster)
    {
        summon = monster;

        return this;
    }

    public void QueueActions(AbstractCreature target)
    {
        if (summon != null)
        {
            GameActions.Bottom.Add(new SummonMonsterAction(summon, false));
            disabled = true;
        }
    }
}
