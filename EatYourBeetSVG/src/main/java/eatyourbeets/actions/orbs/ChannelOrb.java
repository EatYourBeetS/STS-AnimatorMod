package eatyourbeets.actions.orbs;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.FuncT0;

import java.util.ArrayList;

public class ChannelOrb extends EYBActionWithCallback<ArrayList<AbstractOrb>>
{
    private final ArrayList<AbstractOrb> channeledOrbs = new ArrayList<>();
    private FuncT0<AbstractOrb> orbConstructor;
    private AbstractOrb orb;
    private boolean autoEvoke;

    public ChannelOrb(AbstractOrb orb)
    {
        super(ActionType.SPECIAL, Settings.FAST_MODE ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        this.orb = orb;
        this.orbConstructor = null;
        this.autoEvoke = true;

        Initialize(1);
    }

    public ChannelOrb(FuncT0<AbstractOrb> orbConstructor, int amount)
    {
        super(ActionType.SPECIAL, Settings.FAST_MODE ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        this.orb = null;
        this.orbConstructor = orbConstructor;
        this.autoEvoke = true;

        Initialize(amount);
    }

    public ChannelOrb AutoEvoke(boolean autoEvoke)
    {
        this.autoEvoke = autoEvoke;

        return this;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (!TickDuration(deltaTime))
        {
            return;
        }

        if (amount > 0)
        {
            if (orbConstructor != null)
            {
                orb = orbConstructor.Invoke();
            }

            if (autoEvoke)
            {
                AbstractDungeon.player.channelOrb(orb);
                channeledOrbs.add(orb);
            }
            else
            {
                for (AbstractOrb o : AbstractDungeon.player.orbs)
                {
                    if (o instanceof EmptyOrbSlot)
                    {
                        AbstractDungeon.player.channelOrb(orb);
                        channeledOrbs.add(orb);
                        break;
                    }
                }
            }

            amount -= 1;
        }

        if (amount > 0)
        {
            //repeat
            duration = startDuration;
            isDone = false;
        }
        else
        {
            Complete(channeledOrbs);
        }
    }
}
