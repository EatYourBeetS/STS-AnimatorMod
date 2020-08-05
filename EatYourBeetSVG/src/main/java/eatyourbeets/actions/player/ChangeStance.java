package eatyourbeets.actions.player;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.CannotChangeStancePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.JUtils;

public class ChangeStance extends EYBActionWithCallback<Boolean>
{
    private String id;
    private AbstractStance newStance;
    private boolean requireNeutralStance;
    private boolean triggerOnSameStance;

    public ChangeStance(String stanceId)
    {
        super(ActionType.SPECIAL);

        this.id = stanceId;
    }

    public ChangeStance(AbstractStance stance)
    {
        this(stance.ID);

        this.newStance = stance;
    }

    public ChangeStance RequireNeutralStance(boolean value)
    {
        this.requireNeutralStance = value;

        return this;
    }

    public ChangeStance TriggerOnSameStance(boolean value)
    {
        this.triggerOnSameStance = value;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (player.hasPower(CannotChangeStancePower.POWER_ID))
        {
            Complete(false);
            return;
        }

        if (requireNeutralStance && !player.stance.ID.equals(NeutralStance.STANCE_ID))
        {
            Complete(false);
            return;
        }

        AbstractStance oldStance = player.stance;
        if (oldStance.ID.equals(id))
        {
            Complete(triggerOnSameStance);
            return;
        }

        if (newStance == null)
        {
            newStance = AbstractStance.getStanceFromName(id);
        }

        for (AbstractPower p : player.powers)
        {
            p.onChangeStance(oldStance, newStance);
        }

        for (AbstractRelic r : player.relics)
        {
            r.onChangeStance(oldStance, newStance);
        }

        oldStance.onExitStance();
        player.stance = newStance;
        newStance.onEnterStance();

        JUtils.IncrementMapElement(AbstractDungeon.actionManager.uniqueStancesThisCombat, newStance.ID);

        player.switchedStance();

        for (AbstractCard c : player.discardPile.group)
        {
            c.triggerExhaustedCardsOnStanceChange(newStance);
        }

        player.onStanceChange(this.id);

        AbstractDungeon.onModifyPower();

        if (Settings.FAST_MODE)
        {
            Complete(true);
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete(true);
        }
    }
}
