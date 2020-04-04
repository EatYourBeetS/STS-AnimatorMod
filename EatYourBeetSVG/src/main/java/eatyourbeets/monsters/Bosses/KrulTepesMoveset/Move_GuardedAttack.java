package eatyourbeets.monsters.Bosses.KrulTepesMoveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_GuardedAttack extends EYBAbstractMove
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

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK_DEFEND, damageInfo.base);
    }

    public void QueueActions(AbstractCreature target)
    {
        damageInfo.applyPowers(owner, target);
        GameActions.Bottom.Add(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        GameActions.Bottom.GainBlock(owner, BLOCK_AMOUNT);
    }
}