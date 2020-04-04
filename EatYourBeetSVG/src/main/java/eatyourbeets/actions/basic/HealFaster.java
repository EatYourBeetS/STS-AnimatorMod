package eatyourbeets.actions.basic;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameEffects;

public class HealFaster extends EYBAction
{
    protected boolean showEffect = true;

    public HealFaster(AbstractCreature source, AbstractCreature target, int amount)
    {
        super(ActionType.HEAL, Settings.ACTION_DUR_FAST);

        this.canCancel = false;

        Initialize(target, source, amount);
    }

    public HealFaster ShowEffect(boolean showEffect)
    {
        this.showEffect = showEffect;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (amount <= 0)
        {
            Complete();
            return;
        }

        this.target.heal(this.amount, false);

        if (showEffect)
        {
            if (target.isPlayer)
            {
                AbstractDungeon.topPanel.panelHealEffect();
            }

            GameEffects.Queue.Add(new HealEffect(target.hb.cX - target.animX, target.hb.cY, this.amount));
        }
    }
}