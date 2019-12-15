package eatyourbeets.actions.basic;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameEffects;

public class GainTemporaryHP extends EYBActionWithCallback<AbstractCreature>
{
    public GainTemporaryHP(AbstractCreature target, AbstractCreature source, int amount)
    {
        super(ActionType.HEAL, Settings.ACTION_DUR_FAST);

        Initialize(source, target, amount);
    }

    public GainTemporaryHP(AbstractCreature target, AbstractCreature source, int amount, boolean superFast)
    {
        super(ActionType.HEAL, superFast ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        Initialize(source, target, amount);
    }

    @Override
    protected void FirstUpdate()
    {
        if (!this.target.isDying && !this.target.isDead)
        {
            TempHPField.tempHp.set(this.target, TempHPField.tempHp.get(this.target) + this.amount);
            if (this.amount > 0)
            {
                AbstractDungeon.effectsQueue.add(new HealEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY, this.amount));
                this.target.healthBarUpdatedEvent();
            }
        }
    }

    @Override
    protected void UpdateInternal()
    {
        tickDuration();

        if (isDone)
        {
            Complete(target);
        }
    }
}
