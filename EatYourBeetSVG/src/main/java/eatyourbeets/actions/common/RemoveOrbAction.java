package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import eatyourbeets.actions.animator.AnimatorAction;

import java.util.ArrayList;

public class RemoveOrbAction extends AnimatorAction
{
    private final AbstractOrb orb;

    public RemoveOrbAction(AbstractOrb orb)
    {
        this.orb = orb;
    }

    public void update()
    {
        ArrayList<AbstractOrb> orbs = AbstractDungeon.player.orbs;

        int index = orbs.indexOf(orb);
        if (index > 0)
        {
            orbs.set(index, new EmptyOrbSlot());
        }

        this.isDone = true;
    }
}