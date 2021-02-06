package eatyourbeets.monsters;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;

public class EnemyMoveDetails
{
    private static FieldInfo<EnemyMoveInfo> _move = JUtils.GetField("move", AbstractMonster.class);

    public final AbstractMonster enemy;
    public final AbstractMonster.Intent intent;
    public final EnemyMoveInfo move;
    public final boolean isAttacking;

    public EnemyMoveDetails(AbstractMonster enemy)
    {
        this.enemy = enemy;
        this.intent = enemy.intent;
        this.move = _move.Get(enemy);
        this.isAttacking = move.baseDamage >= 0; // It is -1 if not attacking
    }

    public int GetDamage(boolean multi)
    {
        return isAttacking ? ((multi ? GetDamageMulti() : 1) * enemy.getIntentDmg()) : 0;
    }

    public int GetBaseDamage(boolean multi)
    {
        return isAttacking ? ((multi ? GetDamageMulti() : 1) * enemy.getIntentBaseDmg()) : 0;
    }

    public int GetDamageMulti()
    {
        return isAttacking ? (move.isMultiDamage ? move.multiplier : 1) : 0;
    }
}
