package eatyourbeets.actions.orbs;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

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
            if (GameUtilities.IsValidOrb(player.orbs.get(i)))
            {
                randomOrbs.Add(player.orbs.get(i));
            }
        }

        while (randomOrbs.Size() >= 2)
        {
            AbstractOrb orb1 = randomOrbs.Retrieve(AbstractDungeon.cardRandomRng);
            AbstractOrb orb2 = randomOrbs.Retrieve(AbstractDungeon.cardRandomRng);

            int index1 = player.orbs.indexOf(orb1);
            int index2 = player.orbs.indexOf(orb2);

            player.orbs.set(index1, orb2);
            player.orbs.set(index2, orb1);

            orb1.tX = orb2.cX;
            orb1.tY = orb2.cY;
            orb2.tX = orb1.cX;
            orb2.tY = orb1.cY;
        }

        if (amount > 1)
        {
            GameActions.Bottom.Add(new ShuffleOrbs(amount - 1));
        }

        Complete();
    }
}
