package eatyourbeets.actions.orbs;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.function.Predicate;

public class EvokeOrb extends EYBAction
{
    public enum Mode
    {
        Sequential,
        Random,
        SameOrb
    }

    protected Mode mode;
    protected AbstractOrb orb;
    protected Predicate<AbstractOrb> filter;

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

    public EvokeOrb SetFilter(Predicate<AbstractOrb> filter)
    {
        this.filter = filter;

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
                }
                if (amount > 0)
                {
                    GameActions.Top.Add(new EvokeSpecificOrbAction(orb));
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
                }
            }
        }

        Complete();
    }

    protected boolean CheckOrb(AbstractOrb orb)
    {
        return GameUtilities.IsValidOrb(orb) && filter != null && filter.test(orb);
    }
}
