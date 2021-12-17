package pinacolada.actions.orbs;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.utilities.GenericCondition;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class TriggerOrbPassiveAbility extends EYBActionWithCallback<ArrayList<AbstractOrb>>
{
    protected final ArrayList<AbstractOrb> orbs = new ArrayList<>();
    protected GenericCondition<AbstractOrb> filter;
    protected boolean isSequential;
    protected boolean isRandom;
    protected AbstractOrb orb;

    public TriggerOrbPassiveAbility(int times)
    {
        this(times, false, false, null);
    }

    public TriggerOrbPassiveAbility(AbstractOrb orb, int times)
    {
        this(times, false, false, orb);
    }

    public TriggerOrbPassiveAbility(int times, boolean random, boolean sequential, AbstractOrb orb)
    {
        super(ActionType.WAIT);

        this.isRandom = random;
        this.isSequential = sequential;
        this.orb = orb;

        Initialize(times);
    }

    public TriggerOrbPassiveAbility SetFilter(FuncT1<Boolean, AbstractOrb> filter)
    {
        this.filter = GenericCondition.FromT1(filter);

        return this;
    }

    public <S> TriggerOrbPassiveAbility SetFilter(S state, FuncT2<Boolean, S, AbstractOrb> filter)
    {
        this.filter = GenericCondition.FromT2(filter, state);

        return this;
    }

    public TriggerOrbPassiveAbility SetOrb(AbstractOrb orb)
    {
        this.orb = orb;

        return this;
    }

    public TriggerOrbPassiveAbility SetRandom(boolean random)
    {
        this.isRandom = random;

        return this;
    }

    public TriggerOrbPassiveAbility SetSequential(boolean sequential)
    {
        this.isSequential = sequential;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (player.orbs == null || player.orbs.isEmpty())
        {
            Complete();
            return;
        }

        if (orb != null)
        {
            TriggerPassiveEffect(orb, amount);
        }
        else if (isRandom)
        {
            final RandomizedList<AbstractOrb> randomOrbs = new RandomizedList<>();
            for (AbstractOrb temp : player.orbs)
            {
                if (IsValidOrb(temp))
                {
                    randomOrbs.Add(temp);
                }
            }

            final int max = Math.min(randomOrbs.Size(), amount);
            for (int i = 0; i < max; i++)
            {
                TriggerPassiveEffect(randomOrbs.Retrieve(rng, false), 1);
            }
        }
        else if (isSequential)
        {
            int i = 0;
            int orbs = 0;
            while (orbs < amount && i < player.orbs.size())
            {
                final AbstractOrb orb = player.orbs.get(i++);
                if (IsValidOrb(orb))
                {
                    TriggerPassiveEffect(orb, 1);
                    orbs += 1;
                }
            }
        }
        else
        {
            int i = 0;
            while (i < player.orbs.size())
            {
                final AbstractOrb orb = player.orbs.get(i++);
                if (IsValidOrb(orb))
                {
                    TriggerPassiveEffect(orb, amount);
                    break;
                }
            }
        }

        Complete(orbs);
    }

    protected void TriggerPassiveEffect(AbstractOrb orb, int times)
    {
        if (IsValidOrb(orb))
        {
            for (int i = 0; i < times; i++)
            {
                orb.onStartOfTurn();
                orb.onEndOfTurn();
                orbs.add(orb);
            }
        }
    }

    protected boolean IsValidOrb(AbstractOrb orb)
    {
        return PCLGameUtilities.IsValidOrb(orb) && (filter == null || filter.Check(orb));
    }
}
