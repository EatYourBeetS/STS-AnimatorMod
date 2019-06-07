package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;

public class Move_AttackMultipleWeak extends AbstractMove
{
    private final int times;
    private final int debuffAmount;

    public Move_AttackMultipleWeak(int damageAmount, int times, int debuffAmount)
    {
        this.damageInfo = new DamageInfo(owner, damageAmount + GetBonus(damageAmount, 0.2f));
        this.times = times;
        this.debuffAmount = debuffAmount;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK_DEBUFF, damageInfo.base, times, true);
    }

    public void Execute(AbstractPlayer target)
    {
        owner.useFastAttackAnimation();
        damageInfo.applyPowers(owner, target);

        for (int i = 0 ; i < times; i++)
        {
            GameActionsHelper.AddToBottom(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }

        GameActionsHelper.ApplyPower(owner, target, new WeakPower(target, debuffAmount, true), debuffAmount);
    }
}