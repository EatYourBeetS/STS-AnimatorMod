package eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.monsters.AbstractMove;

public class Move_AttackWeakAndStrLoss extends AbstractMove
{
    private boolean usedOnce = false;
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
        GameActionsHelper_Legacy.AddToBottom(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.FIRE));

        if (!usedOnce)
        {
            int str = GameUtilities.GetStrength(target);
            LoseStrengthPower loseStr = JavaUtilities.SafeCast(target.getPower(LoseStrengthPower.POWER_ID), LoseStrengthPower.class);
            if (loseStr != null)
            {
                str -= loseStr.amount;
            }

            if (str > 0)
            {
                GameActionsHelper_Legacy.ApplyPower(owner, target, new StrengthPower(target, -debuffAmount), -debuffAmount);
                usedOnce = true;
            }
        }

        GameActionsHelper_Legacy.ApplyPower(owner, target, new WeakPower(target, debuffAmount, true), debuffAmount);
    }
}