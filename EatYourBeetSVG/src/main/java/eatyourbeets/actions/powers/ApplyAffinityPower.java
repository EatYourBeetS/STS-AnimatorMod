package eatyourbeets.actions.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.powers.common.TemporaryStatsPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.HashMap;

public class ApplyAffinityPower extends ApplyPower
{
    public boolean showEffect;
    public boolean playSFX;
    public boolean retain;

    public ApplyAffinityPower(AbstractCreature source, Affinity affinity, int amount)
    {
        this(source, affinity, amount, false);
    }

    public ApplyAffinityPower(AbstractCreature source, Affinity affinity, int amount, boolean retain)
    {
        super(source, AbstractDungeon.player, CreatePower(affinity, source), amount);

        this.retain = retain;

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
        final AbstractAffinityPower powerToApply = (AbstractAffinityPower) this.powerToApply;

        // Animator classic has the abstract affinity power applied directly to them
        if (GR.AnimatorClassic.IsSelected())
        {
            if (retain)
            {
                powerToApply.RetainOnce();
            }

            if (amount == 0)
            {
                Complete();
                return;
            }

            super.FirstUpdate();
            return;
        }

        // Affinity animator gets the affinity bonus and temporary power (if retain)
        if (retain)
        {
            GameActions.Bottom.GainTemporaryStats(amount, powerToApply.GetThresholdBonusPower());
        }

        CombatStats.Affinities.AddAffinity(powerToApply.affinity, amount);
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (!GR.Animator.IsSelected())
        {
            super.UpdateInternal(deltaTime);
        }
        else if (TickDuration(deltaTime))
        {
            Complete(powerToApply);
        }
    }

    @Override
    protected void AddPower()
    {
        powerToApply.stackPower(amount);

        super.AddPower();
    }

    protected static AbstractAffinityPower CreatePower(Affinity affinity, AbstractCreature source)
    {
        if (!GR.Animator.IsSelected())
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
