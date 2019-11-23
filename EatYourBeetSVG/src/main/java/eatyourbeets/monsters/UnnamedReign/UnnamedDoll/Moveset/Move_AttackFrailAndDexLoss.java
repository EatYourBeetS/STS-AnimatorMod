package eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.monsters.AbstractMove;

public class Move_AttackFrailAndDexLoss extends AbstractMove
{
    private boolean usedOnce = false;
    private final int debuffAmount;

    public Move_AttackFrailAndDexLoss(int damageAmount, int debuffAmount)
    {
        this.debuffAmount = debuffAmount;
        damageInfo = new DamageInfo(owner, damageAmount, DamageInfo.DamageType.NORMAL);
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

        if (!usedOnce)
        {
            int dex = GameUtilities.GetDexterity(target);
            LoseDexterityPower loseDex = JavaUtilities.SafeCast(target.getPower(LoseDexterityPower.POWER_ID), LoseDexterityPower.class);
            if (loseDex != null)
            {
                dex -= loseDex.amount;
            }

            if (dex > 0)
            {
                GameActionsHelper.ApplyPower(owner, target, new DexterityPower(target, -debuffAmount), -debuffAmount);
                usedOnce = true;
            }
        }

        GameActionsHelper.ApplyPower(owner, target, new FrailPower(target, debuffAmount, true), debuffAmount);
    }
}