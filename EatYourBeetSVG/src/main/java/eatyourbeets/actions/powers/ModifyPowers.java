package eatyourbeets.actions.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GenericCondition;
import eatyourbeets.utilities.ListSelection;

import java.util.ArrayList;
import java.util.Comparator;

public class ModifyPowers extends EYBActionWithCallback<ArrayList<AbstractPower>>
{
    protected final ArrayList<AbstractPower> result = new ArrayList<>();
    protected FuncT1<Integer, AbstractPower> getNewAmount;
    protected Comparator<AbstractPower> comparator;
    protected ListSelection<AbstractPower> selection;
    protected GenericCondition<AbstractPower> filter;
    protected boolean isDebuffInteraction;
    protected boolean relative;
    protected int count;

    public ModifyPowers(AbstractCreature target, AbstractCreature source, FuncT1<Integer, AbstractPower> getNewAmount)
    {
        super(ActionType.POWER);

        this.getNewAmount = getNewAmount;
        this.relative = false;
        this.count = 1;

        Initialize(target, source, amount);
    }

    public ModifyPowers(AbstractCreature target, AbstractCreature source, int amount, boolean relative)
    {
        super(ActionType.POWER);

        this.relative = relative;
        this.count = 1;

        Initialize(target, source, amount);
    }

    public ModifyPowers IsDebuffInteraction(boolean value)
    {
        isDebuffInteraction = value;

        return this;
    }

    public ModifyPowers Sort(Comparator<AbstractPower> comparator)
    {
        this.comparator = comparator;

        return this;
    }

    public ModifyPowers SetFilter(FuncT1<Boolean, AbstractPower> filter)
    {
        this.filter = GenericCondition.FromT1(filter);

        return this;
    }

    public <S> ModifyPowers SetFilter(S state, FuncT2<Boolean, S, AbstractPower> filter)
    {
        this.filter = GenericCondition.FromT2(filter, state);

        return this;
    }

    public ModifyPowers SetSelection(ListSelection<AbstractPower> selection, int count)
    {
        this.selection = selection;
        this.count = count;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        final ArrayList<AbstractPower> powers = new ArrayList<>();
        for (AbstractPower p : target.powers)
        {
            if (getNewAmount != null)
            {
                amount = getNewAmount.Invoke(p);
                relative = false;
            }

            if ((CanStack(p) || (!relative && amount == 0)) && (filter == null || filter.Check(p)))
            {
                powers.add(p);
            }
        }

        if (comparator != null)
        {
            powers.sort(comparator);
        }

        if (selection == null)
        {
            selection = ListSelection.Default(0);
        }

        selection.ForEach(powers, count, this::Modify);

        Complete(result);
    }

    protected boolean CanStack(AbstractPower power)
    {
        return !power.canGoNegative || power.amount != -1;
    }

    protected void Modify(AbstractPower power)
    {
        if (getNewAmount != null)
        {
            amount = getNewAmount.Invoke(power);
            relative = false;
        }

        if (!CanStack(power))
        {
            if (!relative && amount == 0)
            {
                GameActions.Bottom.RemovePower(source, power)
                .IsDebuffInteraction(isDebuffInteraction);
                result.add(power);
            }

            return;
        }

        final int stacks = relative ? amount : (amount - power.amount);
        if (stacks >= 0)
        {
            GameActions.Bottom.IncreasePower(power, stacks)
            .IsDebuffInteraction(isDebuffInteraction);
        }
        else
        {
            GameActions.Bottom.ReducePower(power, -stacks)
            .IsDebuffInteraction(isDebuffInteraction);
        }

        result.add(power);
    }
}