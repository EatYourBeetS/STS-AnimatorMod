package eatyourbeets.actions.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class ApplyAffinityPower extends ApplyPower
{
    public boolean showEffect;
    public boolean playSFX;
    public boolean retain;

    private boolean ignoreAffinity;

    public ApplyAffinityPower(AbstractCreature source, Affinity affinity, int amount)
    {
        this(source, affinity, amount, false);
    }

    public ApplyAffinityPower(AbstractCreature source, Affinity affinity, int amount, boolean retain)
    {
        super(source, AbstractDungeon.player, CreatePower(affinity, source), amount);

        this.retain = retain;
        this.ignoreAffinity = !CombatStats.Affinities.isActive;

        if (powerToApply == null)
        {
            Complete();
        }
    }

    public ApplyAffinityPower Retain(boolean retain)
    {
        this.retain = retain;

        return this;
    }

    public ApplyAffinityPower ShowEffect(boolean showEffect, boolean playSFX)
    {
        this.showEffect = showEffect;
        this.playSFX = playSFX;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (ignoreAffinity)
        {
            super.FirstUpdate();
            return;
        }

        final AbstractAffinityPower powerToApply = (AbstractAffinityPower) this.powerToApply;
        if (amount == 0)
        {
            if (retain)
            {
                powerToApply.RetainOnce();
            }

            Complete();
            return;
        }

        if (shouldCancelAction() || !GameUtilities.CanApplyPower(source, target, powerToApply, this))
        {
            Complete(powerToApply);
            return;
        }

        if (source != null)
        {
            for (AbstractPower power : source.powers)
            {
                power.onApplyPower(this.powerToApply, target, source);
            }
        }

        powerToApply.Stack(amount, retain);

        if (playSFX)
        {
            powerToApply.flash();
        }
        else
        {
            powerToApply.flashWithoutSound();
        }

        if (showEffect)
        {
            GameEffects.List.Add(new PowerBuffEffect(target.hb.cX - target.animX, target.hb.cY + target.hb.height / 2f, "+" + amount + " " + powerToApply.name));
        }

        AbstractDungeon.onModifyPower();
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (ignoreAffinity)
        {
            super.UpdateInternal(deltaTime);
            return;
        }

        if (TickDuration(deltaTime))
        {
            Complete(powerToApply);
        }
    }

    @Override
    protected void Complete(AbstractPower result)
    {
        if (ignoreAffinity && retain)
        {
            ((AbstractAffinityPower)result).RetainOnce();
        }

        super.Complete(result);
    }

    @Override
    protected void AddPower()
    {
        powerToApply.stackPower(amount);

        super.AddPower();
    }

    protected static AbstractAffinityPower CreatePower(Affinity affinity, AbstractCreature source)
    {
        if (!CombatStats.Affinities.isActive)
        {
            AbstractAffinityPower result = CombatStats.Affinities.GetPower(affinity);
            result.owner = source;
            CombatStats.ApplyPowerPriority(result);
            return result;
        }
        else if (affinity != null)
        {
            return CombatStats.Affinities.GetPower(affinity);
        }
        else
        {
            return GameUtilities.GetRandomElement(CombatStats.Affinities.Powers, EYBCard.rng);
        }
    }
}
