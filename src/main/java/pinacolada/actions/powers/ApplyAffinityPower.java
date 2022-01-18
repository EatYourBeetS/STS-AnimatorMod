package pinacolada.actions.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.affinity.AbstractPCLAffinityPower;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class ApplyAffinityPower extends EYBActionWithCallback<AbstractPower>
{
    public AbstractPCLAffinityPower power;
    public boolean showEffect;
    public boolean maintain;

    public ApplyAffinityPower(AbstractCreature source, PCLAffinity affinity, int amount)
    {
        this(source, affinity, amount, false);
    }

    public ApplyAffinityPower(AbstractCreature source, PCLAffinity affinity, int amount, boolean maintain)
    {
        super(ActionType.POWER, Settings.ACTION_DUR_XFAST);

        this.maintain = maintain;

        if (affinity != null)
        {
            this.power = PCLCombatStats.MatchingSystem.GetPower(affinity);
        }
        else
        {
            this.power = PCLGameUtilities.GetRandomElement(PCLCombatStats.MatchingSystem.Powers, PCLCard.rng);
        }

        if (power == null || AbstractDungeon.getMonsters().areMonstersBasicallyDead())
        {
            Complete();
            return;
        }

        Initialize(source, power.owner, amount);
    }

    public ApplyAffinityPower Maintain(boolean maintain)
    {
        this.maintain = maintain;

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
        if (maintain)
        {
            power.Maintain();
        }
        if (amount == 0)
        {
            Complete();
            return;
        }

        if (shouldCancelAction() || !PCLGameUtilities.CanApplyPower(source, target, power, this))
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

        power.Stack(amount, maintain);
        power.flash();

        if (showEffect)
        {
            PCLGameEffects.List.Add(new PowerBuffEffect(target.hb.cX - target.animX, target.hb.cY + target.hb.height / 2f, "+" + amount + " " + power.name));
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
