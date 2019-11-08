package eatyourbeets.actions.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import eatyourbeets.powers.animator.EvePower;

public class EveDamageAction extends AnimatorAction
{
    private final AbstractCreature owner;
    private final EvePower power;

    public EveDamageAction(AbstractCreature owner, int amount)
    {
        this.owner = owner;
        this.power = null;
        this.amount = amount;
    }

    public EveDamageAction(EvePower power)
    {
        this.owner = power.owner;
        this.power = power;
        this.amount = power.amount;
    }

    @Override
    public void update()
    {
        AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (target != null)
        {
            AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.SKY)));

            if (Settings.FAST_MODE)
            {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(target.hb.cX, target.hb.cY, owner.hb.cX, owner.hb.cY), 0.1F));
            }
            else
            {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(target.hb.cX, target.hb.cY, owner.hb.cX, owner.hb.cY), 0.3F));
            }

            AbstractDungeon.actionManager.addToBottom(new DamageAction(target, new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS)));

            if (power != null)
            {
                power.flash();
                power.amount += power.growth;
                power.updateDescription();
            }
        }

        this.isDone = true;
    }
}