package eatyourbeets.actions.basic;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.Mathf;

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
        if (!target.isDying && !target.isDead)
        {
            TempHPField.tempHp.set(target, Mathf.Max(0, TempHPField.tempHp.get(target) + amount));
            if (amount > 0)
            {
                GameEffects.Queue.Add(new HealEffect(target.hb.cX - target.animX, target.hb.cY, amount));
                target.healthBarUpdatedEvent();
            }
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete(target);
        }
    }
}
