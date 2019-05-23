package eatyourbeets.monsters.Bosses.TheUnnamedMoveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.Bosses.TheUnnamed;

public class Move_MultiAttack extends Move
{
    private final DamageInfo damageInfo;

    private final int TIMES;

    public Move_MultiAttack(int id, int ascensionLevel, TheUnnamed owner)
    {
        super((byte) id, ascensionLevel, owner);

        damageInfo = new DamageInfo(owner, 7);
        TIMES = 3;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK, damageInfo.base, TIMES, true);
    }

    public void Execute(AbstractPlayer target)
    {
        damageInfo.applyPowers(owner, target);
        for (int i = 0; i < TIMES; i++)
        {
            GameActionsHelper.AddToBottom(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
    }
}
