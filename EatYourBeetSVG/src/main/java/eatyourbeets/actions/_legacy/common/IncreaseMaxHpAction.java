package eatyourbeets.actions._legacy.common;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

public class IncreaseMaxHpAction extends HealAction
{
    private final boolean showEffect;

    public IncreaseMaxHpAction(AbstractCreature target, int amount, boolean showEffect)
    {
        super(target, target, amount);

        if (Settings.FAST_MODE)
        {
            this.startDuration = Settings.ACTION_DUR_XFAST;
        }
        else
        {
            this.startDuration = Settings.ACTION_DUR_FAST;
        }

        this.duration = this.startDuration;
        this.showEffect = showEffect;
        this.amount = amount;
        this.target = target;
    }

    public void update()
    {
        if (this.duration == this.startDuration)
        {
            this.target.increaseMaxHp(amount, showEffect);
        }

        this.tickDuration();
    }
}
