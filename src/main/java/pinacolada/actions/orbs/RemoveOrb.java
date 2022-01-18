package pinacolada.actions.orbs;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.EYBAction;

public class RemoveOrb extends EYBAction
{
    private final AbstractOrb orb;

    public RemoveOrb(AbstractOrb orb)
    {
        super(ActionType.SPECIAL);

        this.orb = orb;
    }

    @Override
    protected void FirstUpdate()
    {
        if (player.orbs.remove(orb))
        {
            player.orbs.add(0, orb);
            player.removeNextOrb();
        }

        Complete();
    }
}
