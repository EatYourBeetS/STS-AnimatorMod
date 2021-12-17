package pinacolada.actions.orbs;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class ShuffleOrbs extends EYBAction
{
    public ShuffleOrbs(int times)
    {
        super(ActionType.WAIT);

        Initialize(times);
    }

    @Override
    protected void FirstUpdate()
    {
        if (player.orbs == null || player.orbs.size() < 2)
        {
            Complete();
            return;
        }

        RandomizedList<AbstractOrb> randomOrbs = new RandomizedList<>();
        for (int i = 0; i < player.orbs.size(); i++)
        {
            if (PCLGameUtilities.IsValidOrb(player.orbs.get(i)))
            {
                randomOrbs.Add(player.orbs.get(i));
            }
        }

        while (randomOrbs.Size() >= 2)
        {
            AbstractOrb orb1 = randomOrbs.Retrieve(rng);
            AbstractOrb orb2 = randomOrbs.Retrieve(rng);

            int index1 = player.orbs.indexOf(orb1);
            int index2 = player.orbs.indexOf(orb2);

            player.orbs.set(index1, orb2);
            player.orbs.set(index2, orb1);

            orb1.setSlot(index2, player.maxOrbs);
            orb2.setSlot(index1, player.maxOrbs);
        }

        if (amount > 1)
        {
            PCLActions.Bottom.Add(new ShuffleOrbs(amount - 1));
        }

        Complete();
    }
}
