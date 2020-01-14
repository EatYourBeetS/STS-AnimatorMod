package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.interfaces.subscribers.OnStatsClearedSubscriber;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;

import java.util.HashSet;

public abstract class PlayerAttributePower extends CommonPower
{
    protected static final PreservedPowers preservedPowers = new PreservedPowers();

    public PlayerAttributePower(String powerID, AbstractCreature owner, int amount)
    {
        super(owner, powerID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        GainPower(amount);
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        GainPower(stackAmount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        if (enabled)
        {
            ReducePower(1);
            GameActions.Bottom.ReducePower(this, 1);
        }
    }

    @Override
    public void update(int slot)
    {
        super.update(slot);

        enabled = (!preservedPowers.contains(ID));
    }

    protected abstract void GainPower(int amount);
    protected abstract void ReducePower(int amount);

    public static class PreservedPowers extends HashSet<String> implements OnStatsClearedSubscriber, OnStartOfTurnPostDrawSubscriber
    {
        @Override
        public void OnStatsCleared()
        {
            clear();

            PlayerStatistics.onStartOfTurnPostDraw.Unsubscribe(this);
            PlayerStatistics.onStatsCleared.Unsubscribe(this);
        }

        @Override
        public void OnStartOfTurnPostDraw()
        {
            clear();

            PlayerStatistics.onStartOfTurnPostDraw.Unsubscribe(this);
            PlayerStatistics.onStatsCleared.Unsubscribe(this);
        }

        public void Subscribe(String powerID)
        {
            add(powerID);

            PlayerStatistics.onStatsCleared.Subscribe(this);
            PlayerStatistics.onStartOfTurnPostDraw.Subscribe(this);
        }
    }
}