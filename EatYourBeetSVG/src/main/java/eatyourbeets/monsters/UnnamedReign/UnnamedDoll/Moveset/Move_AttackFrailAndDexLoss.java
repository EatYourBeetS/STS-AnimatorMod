package eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public class Move_AttackFrailAndDexLoss extends EYBAbstractMove
{
    private boolean usedOnce = false;
    private final int debuffAmount;

    public Move_AttackFrailAndDexLoss(int damageAmount, int debuffAmount)
    {
        this.debuffAmount = debuffAmount;
        damageInfo = new DamageInfo(owner, damageAmount, DamageInfo.DamageType.NORMAL);
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
            int dex = GameUtilities.GetPowerAmount(target, DexterityPower.POWER_ID);
            LoseDexterityPower loseDex = JavaUtilities.SafeCast(target.getPower(LoseDexterityPower.POWER_ID), LoseDexterityPower.class);
            if (loseDex != null)
            {
                dex -= loseDex.amount;
            }

            if (dex > 0)
            {
                GameActions.Bottom.ApplyPower(owner, target, new DexterityPower(target, -debuffAmount), -debuffAmount);
                usedOnce = true;
            }
        }

        GameActions.Bottom.ApplyFrail(owner, target, debuffAmount);
    }
}