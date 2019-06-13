package eatyourbeets.monsters.Bosses.TheUnnamedMoveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;

public class Move_MultiAttack extends AbstractMove
{
    private final int TIMES;

    public Move_MultiAttack(int damage, int times)
    {
        damageInfo = new DamageInfo(owner, damage);
        TIMES = times;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK, damageInfo.base, TIMES, true);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        boolean superFast = TIMES > 6;

        damageInfo.applyPowers(owner, target);
        for (int i = 0; i < TIMES; i++)
        {
            GameActionsHelper.AddToBottom(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.SLASH_HEAVY, superFast));
        }
    }
}
