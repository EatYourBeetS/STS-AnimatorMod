package eatyourbeets.actions.orbs;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.GenericCondition;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class EvokeOrb extends EYBActionWithCallback<ArrayList<AbstractOrb>>
{
    public enum Mode
    {
        Sequential,
        Random,
        SameOrb
    }

    protected final ArrayList<AbstractOrb> orbs = new ArrayList<>();
    protected GenericCondition<AbstractOrb> filter;
    protected AbstractOrb orb;
    protected Mode mode;

    public EvokeOrb(int times)
    {
        this(times, Mode.SameOrb);
    }

    public EvokeOrb(int times, Mode mode)
    {
        super(ActionType.WAIT);

        this.mode = mode;
        this.orb = null;

        Initialize(times);
    }

    public EvokeOrb(int times, AbstractOrb orb)
    {
        super(ActionType.WAIT);

        this.mode = Mode.SameOrb;
        this.orb = orb;

        Initialize(times);
    }

    public EvokeOrb SetFilter(FuncT1<Boolean, AbstractOrb> filter)
    {
        this.filter = GenericCondition.FromT1(filter);

        return this;
    }

    public <S> EvokeOrb SetFilter(S state, FuncT2<Boolean, S, AbstractOrb> filter)
    {
        this.filter = GenericCondition.FromT2(filter, state);

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (player.orbs == null || player.orbs.isEmpty())
        {
            Complete(orbs);
            return;
        }

        if (mode == Mode.SameOrb)
        {
            boolean checked = false;
            if (orb == null)
            {
                for (int i = 0; i < player.orbs.size(); i++)
                {
                    orb = player.orbs.get(i);
                    if (CheckOrb(orb))
                    {
                        checked = true;
                        break;
                    }
                }
            }

            if (checked || CheckOrb(orb))
            {
                for (int j = 0; j < (amount - 1); j++)
                {
                    orb.onEvoke();
                    orbs.add(orb);
                }
                if (amount > 0)
                {
                    GameActions.Top.Add(new EvokeSpecificOrbAction(orb));
                    orbs.add(orb);
                }
            }
        }
        else if (mode == Mode.Random)
        {
            RandomizedList<AbstractOrb> randomOrbs = new RandomizedList<>();
            for (AbstractOrb temp : player.orbs)
            {
                if (CheckOrb(temp))
                {
                    randomOrbs.Add(temp);
                }
            }

            for (int i = 0; i < amount; i++)
            {
                orb = randomOrbs.Retrieve(rng);
                if (orb != null)
                {
                    GameActions.Top.Add(new EvokeSpecificOrbAction(orb));
                    orbs.add(orb);
                }
            }
        }
        else if (mode == Mode.Sequential)
        {
            int max = Math.min(player.orbs.size(), amount);
            for (int i = 1; i <= max; i++)
            {
                orb = player.orbs.get(i - 1);
                if (CheckOrb(orb))
                {
                    GameActions.Top.Add(new EvokeSpecificOrbAction(orb));
                    orbs.add(orb);
                }
            }
        }

        Complete(orbs);
    }

    protected boolean CheckOrb(AbstractOrb orb)
    {
        return GameUtilities.IsValidOrb(orb) && (filter == null || filter.Check(orb));
    }
}
