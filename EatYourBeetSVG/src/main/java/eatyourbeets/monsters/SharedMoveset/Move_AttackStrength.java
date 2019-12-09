package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.monsters.AbstractMove;

public class Move_AttackStrength extends AbstractMove
{
    private final int buffAmount;

    public Move_AttackStrength(int damageAmount, int buffAmount)
    {
        this.buffAmount = buffAmount;
        damageInfo = new DamageInfo(owner, damageAmount + GetBonus(damageAmount, 0.25f));
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
        GameActionsHelper_Legacy.ApplyPower(owner, owner, new StrengthPower(owner, buffAmount), buffAmount);
    }
}