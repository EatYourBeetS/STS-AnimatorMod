package eatyourbeets.monsters.SharedMoveset_Old;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_Attack extends EYBAbstractMove
{
    public Move_Attack(int amount)
    {
        this.damageInfo = new DamageInfo(owner, amount + CalculateAscensionBonus(amount, 0.25f));
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK, damageInfo.base);
    }

    public void QueueActions(AbstractCreature target)
    {
        owner.useFastAttackAnimation();
        damageInfo.applyPowers(owner, target);
        GameActions.Bottom.Add(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }
}