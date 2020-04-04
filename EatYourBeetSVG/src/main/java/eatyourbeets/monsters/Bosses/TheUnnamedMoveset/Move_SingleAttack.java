package eatyourbeets.monsters.Bosses.TheUnnamedMoveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_SingleAttack extends EYBAbstractMove
{
    public Move_SingleAttack(int damage)
    {
        damageInfo = new DamageInfo(owner, damage);
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK, damageInfo.base);
    }

    public void QueueActions(AbstractCreature target)
    {
        damageInfo.applyPowers(owner, target);
        GameActions.Bottom.Add(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }
}