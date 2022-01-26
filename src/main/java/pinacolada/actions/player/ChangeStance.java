package pinacolada.actions.player;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.CannotChangeStancePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.actions.EYBActionWithCallback;
import pinacolada.stances.PCLStance;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.PCLJUtils;

public class ChangeStance extends EYBActionWithCallback<AbstractStance>
{
    private String id;
    private PCLStanceHelper helper;
    private AbstractStance newStance;
    private AbstractStance previousStance;
    private boolean requireNeutralStance;
    private boolean triggerOnSameStance;

    public ChangeStance(String stanceId)
    {
        super(ActionType.SPECIAL);

        this.id = stanceId;
    }

    public ChangeStance(PCLStanceHelper stanceHelper)
    {
        super(ActionType.SPECIAL);

        this.id = stanceHelper.ID;
        this.helper = stanceHelper;
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
            Complete(null);
            return;
        }

        if (requireNeutralStance && !player.stance.ID.equals(NeutralStance.STANCE_ID))
        {
            Complete(null);
            return;
        }

        previousStance = player.stance;
        if (previousStance.ID.equals(id))
        {
            if (previousStance instanceof PCLStance)
            {
                ((PCLStance) previousStance).onRefreshStance();
            }

            Complete(triggerOnSameStance ? previousStance : null);
            return;
        }

        if (newStance == null)
        {
            newStance = helper != null ? helper.Create() : AbstractStance.getStanceFromName(id);
        }

        for (AbstractPower p : player.powers)
        {
            p.onChangeStance(previousStance, newStance);
        }

        for (AbstractRelic r : player.relics)
        {
            r.onChangeStance(previousStance, newStance);
        }

        previousStance.onExitStance();
        player.stance = newStance;
        newStance.onEnterStance();

        PCLJUtils.IncrementMapElement(AbstractDungeon.actionManager.uniqueStancesThisCombat, newStance.ID);

        player.switchedStance();

        for (AbstractCard c : player.discardPile.group)
        {
            c.triggerExhaustedCardsOnStanceChange(newStance);
        }

        player.onStanceChange(this.id);

        AbstractDungeon.onModifyPower();

        if (Settings.FAST_MODE)
        {
            Complete(previousStance);
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete(previousStance);
        }
    }
}
