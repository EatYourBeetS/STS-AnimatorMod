package eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.powers.PlayerStatistics;

public class Move_AttackWeakAndStrLoss extends AbstractMove
{
    private int debuffDelay = 0;
    private final int debuffAmount;

    public Move_AttackWeakAndStrLoss(int damageAmount, int debuffAmount)
    {
        this.debuffAmount = debuffAmount;
        this.damageInfo = new DamageInfo(owner, damageAmount, DamageInfo.DamageType.NORMAL);
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK_DEBUFF, damageInfo.base);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        owner.useFastAttackAnimation();
        damageInfo.applyPowers(owner, target);
        GameActionsHelper.AddToBottom(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.FIRE));

        if (debuffDelay <= 0)
        {
            int str = PlayerStatistics.GetStrength(target);
            LoseStrengthPower loseStr = Utilities.SafeCast(target.getPower(LoseStrengthPower.POWER_ID), LoseStrengthPower.class);
            if (loseStr != null)
            {
                str -= loseStr.amount;
            }

            if (str >= 0)
            {
                GameActionsHelper.ApplyPower(owner, target, new StrengthPower(target, -debuffAmount), -debuffAmount);
            }

            debuffDelay = 1;
        }
        else
        {
            debuffDelay -= 1;
        }

        GameActionsHelper.ApplyPower(owner, target, new WeakPower(target, debuffAmount, true), debuffAmount);
    }
}