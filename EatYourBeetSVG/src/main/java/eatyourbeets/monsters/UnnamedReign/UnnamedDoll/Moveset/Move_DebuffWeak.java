package eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.TheUnnamed_Doll;

public class Move_DebuffWeak extends Move
{
    private final int DEBUFF_AMOUNT;
    private final DamageInfo damageInfo;

    public Move_DebuffWeak(int id, int ascensionLevel, TheUnnamed_Doll owner, TheUnnamed theUnnamed)
    {
        super((byte) id, ascensionLevel, owner, theUnnamed);

        DEBUFF_AMOUNT = 1;
        damageInfo = new DamageInfo(owner, 1, DamageInfo.DamageType.NORMAL);
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK_DEBUFF, damageInfo.base);
    }

    public void Execute(AbstractPlayer target)
    {
        owner.useFastAttackAnimation();
        damageInfo.applyPowers(owner, target);
        GameActionsHelper.AddToBottom(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.FIRE));
        GameActionsHelper.ApplyPower(owner, target, new WeakPower(target, DEBUFF_AMOUNT, true), DEBUFF_AMOUNT);
    }
}