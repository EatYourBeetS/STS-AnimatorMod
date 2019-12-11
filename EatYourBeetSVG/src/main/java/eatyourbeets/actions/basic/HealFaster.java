package eatyourbeets.actions.basic;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBAction;

public class HealFaster extends EYBAction
{
    protected boolean showEffect = true;

    public HealFaster(AbstractCreature source, AbstractCreature target, int amount)
    {
        super(ActionType.HEAL, Settings.ACTION_DUR_FASTER);

        Initialize(target, source, amount);
    }

    public HealFaster SetOptions(boolean showEffect)
    {
        this.showEffect = showEffect;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        this.target.heal(this.amount);
    }
}