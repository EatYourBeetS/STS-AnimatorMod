package eatyourbeets.monsters.Bosses.TheUnnamedMoveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_MultiAttack extends EYBAbstractMove
{
    private final int TIMES;

    public Move_MultiAttack(int damage, int times)
    {
        damageInfo = new DamageInfo(owner, damage);
        TIMES = times;
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK, damageInfo.base, TIMES, true);
    }

    public void QueueActions(AbstractCreature target)
    {
        boolean superFast = TIMES > 6;

        damageInfo.applyPowers(owner, target);
        for (int i = 0; i < TIMES; i++)
        {
            GameActions.Bottom.Add(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.SLASH_HEAVY, superFast));
        }
    }
}
