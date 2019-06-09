package eatyourbeets.monsters.Bosses.KrulTepesMoveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;

public class Move_GuardedAttack extends AbstractMove
{
    private final int BLOCK_AMOUNT;

    public Move_GuardedAttack()
    {
        if (ascensionLevel >= 6)
        {
            damageInfo = new DamageInfo(owner, 18);
            BLOCK_AMOUNT = 22;
        }
        else
        {
            damageInfo = new DamageInfo(owner, 20);
            BLOCK_AMOUNT = 22;
        }
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK_DEFEND, damageInfo.base);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        damageInfo.applyPowers(owner, target);
        GameActionsHelper.AddToBottom(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        GameActionsHelper.GainBlock(owner, BLOCK_AMOUNT);
    }
}