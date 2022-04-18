package eatyourbeets.actions.basic;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameEffects;

public class GainBlock extends EYBActionWithCallback<AbstractCreature>
{
    protected boolean showVFX;
    protected float pitchMin;
    protected float pitchMax;

    public GainBlock(AbstractCreature target, AbstractCreature source, int amount)
    {
        this(target, source, amount, false);
    }

    public GainBlock(AbstractCreature target, AbstractCreature source, int amount, boolean superFast)
    {
        super(ActionType.BLOCK, superFast ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        this.attackEffect = AttackEffects.SHIELD;
        this.pitchMin = 0.95f;
        this.pitchMax = 1.05f;
        this.showVFX = true;

        Initialize(source, target, amount);

        if (amount <= 0)
        {
            Complete();
        }
    }

    public GainBlock SetFrostShield(boolean value)
    {
        this.attackEffect = value ? AttackEffects.SHIELD_FROST : AttackEffects.SHIELD;

        return this;
    }

    public GainBlock SetVFX(boolean mute, boolean superFast)
    {
        this.startDuration = this.duration = superFast ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST;

        if (mute)
        {
            this.pitchMin = this.pitchMax = 0;
        }

        return this;
    }

    public GainBlock ShowVFX(boolean show)
    {
        this.showVFX = show;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (!target.isDying && !target.isDead && amount > 0)
        {
            if (showVFX)
            {
                GameEffects.List.Attack(source, target, attackEffect, pitchMin, pitchMax, null);
            }

            target.addBlock(amount);
            player.hand.applyPowers();
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
