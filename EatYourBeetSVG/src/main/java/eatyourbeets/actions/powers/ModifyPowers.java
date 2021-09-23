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
    protected Comparator<AbstractPower> comparator;
    protected ListSelection<AbstractPower> selection;
    protected GenericCondition<AbstractPower> filter;
    protected boolean relative;
    protected int count;

    public ModifyPowers(AbstractCreature target, AbstractCreature source, int amount, boolean relative)
    {
        super(ActionType.POWER);

        this.relative = relative;
        this.count = 1;

        Initialize(target, source, amount);
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
        if (!CanStack(power))
        {
            if (!relative && amount == 0)
            {
                GameActions.Bottom.RemovePower(source, power);
                result.add(power);
            }

            return;
        }

        final int stacks = relative ? amount : (amount - power.amount);
        if (stacks >= 0)
        {
            GameActions.Bottom.IncreasePower(power, stacks);
        }
        else
        {
            GameActions.Bottom.ReducePower(power, -stacks);
        }

        result.add(power);
    }
}