package eatyourbeets.actions.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.EYBPower;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class ApplyAffinityPower extends EYBActionWithCallback<AbstractPower>
{
    public AbstractAffinityPower power;
    public boolean showEffect;
    public boolean retain;

    public ApplyAffinityPower(AbstractCreature source, AffinityType affinityType, int amount)
    {
        this(source, affinityType, amount, false);
    }

    public ApplyAffinityPower(AbstractCreature source, AffinityType affinityType, int amount, boolean retain)
    {
        super(ActionType.POWER, Settings.FAST_MODE ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FASTER);

        if (affinityType != null)
        {
            this.power = CombatStats.Affinities.GetPower(affinityType);
        }
        else
        {
            this.power = JUtils.GetRandomElement(CombatStats.Affinities.Powers, EYBPower.rng);
        }

        if (power == null || GameUtilities.GetEnemies(true).isEmpty())
        {
            Complete();
            return;
        }

        Initialize(source, power.owner, amount);
    }

    public ApplyAffinityPower Retain(boolean retain)
    {
        this.retain = retain;

        return this;
    }

    public ApplyAffinityPower ShowEffect(boolean showEffect)
    {
        this.showEffect = showEffect;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (amount == 0)
        {
            Complete();
            return;
        }

        if (shouldCancelAction() || !GameUtilities.CanApplyPower(source, target, power))
        {
            Complete(power);
            return;
        }

        if (source != null)
        {
            for (AbstractPower power : source.powers)
            {
                power.onApplyPower(this.power, target, source);
            }
        }

        power.Stack(amount, retain);
        power.flash();

        if (showEffect)
        {
            GameEffects.List.Add(new PowerBuffEffect(target.hb.cX - target.animX, target.hb.cY + target.hb.height / 2f, "+" + amount + " " + power.name));
        }

        AbstractDungeon.onModifyPower();
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete(power);
        }
    }
}
