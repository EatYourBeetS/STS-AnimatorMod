package eatyourbeets.monsters.SharedMoveset_Old;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_AttackMultipleWeak extends EYBAbstractMove
{
    private final int times;
    private final int debuffAmount;

    public Move_AttackMultipleWeak(int damageAmount, int times, int debuffAmount)
    {
        this.damageInfo = new DamageInfo(owner, damageAmount + CalculateAscensionBonus(damageAmount, 0.2f));
        this.times = times;
        this.debuffAmount = debuffAmount;
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK_DEBUFF, damageInfo.base, times, true);
    }

    public void QueueActions(AbstractCreature target)
    {
        owner.useFastAttackAnimation();
        damageInfo.applyPowers(owner, target);

        for (int i = 0 ; i < times; i++)
        {
            GameActions.Bottom.Add(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }

        GameActions.Bottom.ApplyWeak(owner, target, debuffAmount);
    }
}