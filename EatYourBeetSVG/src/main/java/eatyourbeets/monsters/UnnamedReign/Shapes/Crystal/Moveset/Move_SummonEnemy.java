package eatyourbeets.monsters.UnnamedReign.Shapes.Crystal.Moveset;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.actions.monsters.SummonMonsterAction;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_SummonEnemy extends EYBAbstractMove
{
    private int summonCount = 0;
    private AbstractMonster summon;

    @Override
    public void Initialize(byte id, AbstractMonster owner)
    {
        super.Initialize(id, owner);
    }

    @Override
    public boolean CanUse(Byte previousMove)
    {
        return summonCount == 0;
    }

    public void SetSummon(AbstractMonster monster)
    {
        summon = monster;
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.UNKNOWN);
    }

    public void QueueActions(AbstractCreature target)
    {
        summonCount += 1;

        GameActions.Bottom.Add(new SummonMonsterAction(summon, false));
    }
}
