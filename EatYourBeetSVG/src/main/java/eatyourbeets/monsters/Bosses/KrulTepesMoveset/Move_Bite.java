package eatyourbeets.monsters.Bosses.KrulTepesMoveset;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_Bite extends EYBAbstractMove
{
    private final int WEAK_AMOUNT;

    public Move_Bite()
    {
        if (ascensionLevel >= 6)
        {
            damageInfo = new DamageInfo(owner, 24);
            WEAK_AMOUNT = 2;
        }
        else
        {
            damageInfo = new DamageInfo(owner, 28);
            WEAK_AMOUNT = 2;
        }
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK_DEBUFF, damageInfo.base);
    }

    public void QueueActions(AbstractCreature target)
    {
        damageInfo.applyPowers(owner, target);
        GameActions.Bottom.VFX(new BiteEffect(target.hb.cX, target.hb.cY - 40f * Settings.scale, Color.SCARLET.cpy()), 0.3f);
        GameActions.Bottom.Add(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.NONE));
        GameActions.Bottom.ApplyWeak(owner, target, WEAK_AMOUNT);
    }
}