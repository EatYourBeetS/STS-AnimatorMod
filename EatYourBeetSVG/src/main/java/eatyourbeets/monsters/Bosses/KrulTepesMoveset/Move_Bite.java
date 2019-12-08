package eatyourbeets.monsters.Bosses.KrulTepesMoveset;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.monsters.AbstractMove;

public class Move_Bite extends AbstractMove
{
    private final int STRENGTH_AMOUNT;
    private final int WEAK_AMOUNT;

    public Move_Bite()
    {
        if (ascensionLevel >= 6)
        {
            damageInfo = new DamageInfo(owner, 26);
            STRENGTH_AMOUNT = 2;
            WEAK_AMOUNT = 2;
        }
        else
        {
            damageInfo = new DamageInfo(owner, 28);
            STRENGTH_AMOUNT = 3;
            WEAK_AMOUNT = 2;
        }
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK_DEBUFF, damageInfo.base);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        damageInfo.applyPowers(owner, target);
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(target.hb.cX, target.hb.cY - 40.0F * Settings.scale, Color.SCARLET.cpy()), 0.3F));
        GameActionsHelper.AddToBottom(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.NONE));
        GameActionsHelper.ApplyPower(owner, target, new WeakPower(target, WEAK_AMOUNT, true), WEAK_AMOUNT);
        GameActionsHelper.ApplyPower(owner, owner, new StrengthPower(owner, STRENGTH_AMOUNT), STRENGTH_AMOUNT);
    }
}