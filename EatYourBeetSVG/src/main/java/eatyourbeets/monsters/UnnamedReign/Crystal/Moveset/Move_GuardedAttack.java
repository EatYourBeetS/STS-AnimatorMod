package eatyourbeets.monsters.UnnamedReign.Crystal.Moveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;

public class Move_GuardedAttack extends AbstractMove
{
    private final DamageInfo damageInfo;
    private final int BLOCK_AMOUNT;

    public Move_GuardedAttack(int id, int damageAmount, int blockAmount, AbstractMonster owner)
    {
        super((byte) id, 0, owner);

        damageInfo = new DamageInfo(owner, damageAmount);
        BLOCK_AMOUNT = blockAmount;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK_DEFEND, damageInfo.base);
    }

    public void Execute(AbstractPlayer target)
    {
        owner.useFastAttackAnimation();
        damageInfo.applyPowers(owner, target);
        GameActionsHelper.AddToBottom(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        GameActionsHelper.GainBlock(owner, BLOCK_AMOUNT);
    }
}