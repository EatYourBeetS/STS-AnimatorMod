package eatyourbeets.actions.orbs;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class TriggerOrbPassiveAbility extends EYBActionWithCallback<ArrayList<AbstractOrb>>
{
    protected final ArrayList<AbstractOrb> orbs = new ArrayList<>();
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
            RandomizedList<AbstractOrb> randomOrbs = new RandomizedList<>();
            for (AbstractOrb temp : player.orbs)
            {
                if (GameUtilities.IsValidOrb(temp))
                {
                    randomOrbs.Add(temp);
                }
            }

            for (int i = 0; i < amount; i++)
            {
                TriggerPassiveEffect(randomOrbs.Retrieve(rng), 1);
            }
        }
        else if (isSequential)
        {
            int max = Math.min(player.orbs.size(), amount);
            for (int i = 1; i <= max; i++)
            {
                TriggerPassiveEffect(player.orbs.get(i - 1), 1);
            }
        }
        else
        {
            TriggerPassiveEffect(player.orbs.get(0), amount);
        }

        Complete(orbs);
    }

    protected void TriggerPassiveEffect(AbstractOrb orb, int times)
    {
        if (GameUtilities.IsValidOrb(orb))
        {
            for (int i = 0; i < times; i++)
            {
                orb.onStartOfTurn();
                orb.onEndOfTurn();
                orbs.add(orb);
            }
        }
    }
}
