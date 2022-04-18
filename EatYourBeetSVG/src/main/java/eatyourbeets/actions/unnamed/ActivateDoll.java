package eatyourbeets.actions.unnamed;

import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.monsters.PlayerMinions.UnnamedDoll;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GenericCondition;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class ActivateDoll extends EYBActionWithCallback<ArrayList<UnnamedDoll>>
{
    protected final ArrayList<UnnamedDoll> dolls = new ArrayList<>();
    protected GenericCondition<UnnamedDoll> filter;
    protected boolean isSequential;
    protected boolean isRandom;
    protected UnnamedDoll toActivate;

    public ActivateDoll(int times)
    {
        this(times, false, false, null);
    }

    public ActivateDoll(UnnamedDoll doll, int times)
    {
        this(times, false, false, doll);
    }

    public ActivateDoll(int times, boolean random, boolean sequential, UnnamedDoll specificDoll)
    {
        super(ActionType.WAIT);

        this.isRandom = random;
        this.isSequential = sequential;
        this.toActivate = specificDoll;

        Initialize(times);
    }

    public ActivateDoll SetFilter(FuncT1<Boolean, UnnamedDoll> filter)
    {
        this.filter = GenericCondition.FromT1(filter);

        return this;
    }

    public <S> ActivateDoll SetFilter(S state, FuncT2<Boolean, S, UnnamedDoll> filter)
    {
        this.filter = GenericCondition.FromT2(filter, state);

        return this;
    }

    public ActivateDoll SetDoll(UnnamedDoll doll)
    {
        this.toActivate = doll;

        return this;
    }

    public ActivateDoll SetRandom(boolean random)
    {
        this.isRandom = random;

        return this;
    }

    public ActivateDoll SetSequential(boolean sequential)
    {
        this.isSequential = sequential;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        final ArrayList<UnnamedDoll> list = CombatStats.Dolls.GetAll();
        if (JUtils.IsNullOrEmpty(list))
        {
            Complete(dolls);
            return;
        }

        if (toActivate != null)
        {
            Activate(toActivate, amount);
        }
        else if (isRandom)
        {
            final RandomizedList<UnnamedDoll> randomList = new RandomizedList<>();
            for (UnnamedDoll d : list)
            {
                if (IsValidDoll(d))
                {
                    randomList.Add(d);
                }
            }

            final int max = Math.min(randomList.Size(), amount);
            for (int i = 0; i < max; i++)
            {
                Activate(randomList.Retrieve(rng), 1);
            }
        }
        else if (isSequential)
        {
            int i = 0;
            int activated = 0;
            while (activated < amount && i < list.size())
            {
                final UnnamedDoll d = list.get(i++);
                if (IsValidDoll(d))
                {
                    Activate(d, 1);
                    activated += 1;
                }
            }
        }
        else
        {
            int i = 0;
            while (i < list.size())
            {
                final UnnamedDoll d = list.get(i++);
                if (IsValidDoll(d))
                {
                    Activate(d, amount);
                    break;
                }
            }
        }

        Complete(dolls);
    }

    protected void Activate(UnnamedDoll doll, int times)
    {
        if (IsValidDoll(doll))
        {
            for (int i = 0; i < times; i++)
            {
                CombatStats.Dolls.Activate(doll, false);
                dolls.add(doll);
            }
        }
    }

    protected boolean IsValidDoll(UnnamedDoll doll)
    {
        return filter == null || filter.Check(doll);
    }
}
