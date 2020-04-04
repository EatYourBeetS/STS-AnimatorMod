package eatyourbeets.monsters.SharedMoveset_Old;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_AttackVulnerable extends EYBAbstractMove
{
    private final int debuffAmount;

    public Move_AttackVulnerable(int damageAmount, int debuffAmount)
    {
        this.debuffAmount = debuffAmount;
        damageInfo = new DamageInfo(owner, damageAmount + CalculateAscensionBonus(damageAmount, 0.2f));
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK_DEBUFF, damageInfo.base);
    }

    public void QueueActions(AbstractCreature target)
    {
        owner.useFastAttackAnimation();
        damageInfo.applyPowers(owner, target);
        GameActions.Bottom.Add(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.FIRE));
        GameActions.Bottom.ApplyVulnerable(owner, target, debuffAmount);
    }
}