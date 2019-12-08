package eatyourbeets.actions._legacy.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;

import java.util.ArrayList;

public class RemoveOrbAction extends AbstractGameAction
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