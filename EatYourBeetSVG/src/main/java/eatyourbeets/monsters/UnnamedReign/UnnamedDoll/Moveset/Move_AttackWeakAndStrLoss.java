package eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_AttackWeakAndStrLoss extends EYBAbstractMove
{
    private boolean usedOnce = false;
    private final int debuffAmount;

    public Move_AttackWeakAndStrLoss(int damageAmount, int debuffAmount)
    {
        this.debuffAmount = debuffAmount;
        this.damageInfo = new DamageInfo(owner, damageAmount, DamageInfo.DamageType.NORMAL);
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

        if (!usedOnce)
        {
            int str = GameUtilities.GetPowerAmount(target, StrengthPower.POWER_ID);
            LoseStrengthPower loseStr = JavaUtilities.SafeCast(target.getPower(LoseStrengthPower.POWER_ID), LoseStrengthPower.class);
            if (loseStr != null)
            {
                str -= loseStr.amount;
            }

            if (str > 0)
            {
                GameActions.Bottom.ApplyPower(owner, target, new StrengthPower(target, -debuffAmount), -debuffAmount);
                usedOnce = true;
            }
        }

        GameActions.Bottom.ApplyWeak(owner, target, debuffAmount);
    }
}