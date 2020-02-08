package eatyourbeets.actions.orbs;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class TriggerOrbPassiveAbility extends EYBAction
{
    protected boolean isRandom;

    public TriggerOrbPassiveAbility(int times)
    {
        this(times, false);
    }

    public TriggerOrbPassiveAbility(int times, boolean random)
    {
        super(ActionType.WAIT);

        isRandom = random;

        Initialize(times);
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
                if (temp != null && !EmptyOrbSlot.ORB_ID.equals(temp.ID))
                {
                    randomOrbs.Add(temp);
                }
            }

            orb = randomOrbs.Retrieve(AbstractDungeon.cardRandomRng);
        }
        else
        {
            orb = player.orbs.get(0);
        }

        if (orb != null && !EmptyOrbSlot.ORB_ID.equals(orb.ID))
        {
            orb.onStartOfTurn();
            orb.onEndOfTurn();

            if (amount > 1)
            {
                GameActions.Bottom.Add(new TriggerOrbPassiveAbility(amount - 1, isRandom));
            }
        }

        Complete();
    }
}
