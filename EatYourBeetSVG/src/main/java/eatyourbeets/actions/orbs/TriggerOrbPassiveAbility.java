package eatyourbeets.actions.orbs;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class TriggerOrbPassiveAbility extends EYBAction
{
    protected boolean isRandom;
    protected boolean isSequential;

    public TriggerOrbPassiveAbility(int times)
    {
        this(times, false, false);
    }

    public TriggerOrbPassiveAbility(int times, boolean random, boolean sequential)
    {
        super(ActionType.WAIT);

        isRandom = random;
        isSequential = sequential;

        Initialize(times);
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

        AbstractOrb orb;
        if (isRandom)
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
                orb = randomOrbs.Retrieve(rng);
                orb.onStartOfTurn();
                orb.onEndOfTurn();
            }
        }
        else if (isSequential)
        {
            int max = Math.min(player.orbs.size(), amount);
            for (int i = 1; i <= max; i++)
            {
                orb = player.orbs.get(i - 1);
                if (GameUtilities.IsValidOrb(orb))
                {
                    orb.onStartOfTurn();
                    orb.onEndOfTurn();
                }
            }
        }
        else
        {
            orb = player.orbs.get(0);
            if (GameUtilities.IsValidOrb(orb))
            {
                for (int i = 0; i < amount; i++)
                {
                    orb.onStartOfTurn();
                    orb.onEndOfTurn();
                }
            }
        }

        Complete();
    }
}
