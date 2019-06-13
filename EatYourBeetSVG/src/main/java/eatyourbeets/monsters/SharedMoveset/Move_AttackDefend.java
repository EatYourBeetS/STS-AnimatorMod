package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;

public class Move_AttackDefend extends AbstractMove
{
    private final int blockAmount;

    public Move_AttackDefend(int damageAmount, int blockAmount)
    {
        this.damageInfo = new DamageInfo(owner, damageAmount + GetBonus(damageAmount, 0.25f));
        this.blockAmount = blockAmount + GetBonus(blockAmount, 0.25f);
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK_DEFEND, damageInfo.base);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        owner.useFastAttackAnimation();
        damageInfo.applyPowers(owner, target);
        GameActionsHelper.AddToBottom(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        GameActionsHelper.GainBlock(owner, blockAmount);
    }
}