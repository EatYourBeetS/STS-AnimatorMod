package eatyourbeets.actions.orbs;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class InduceOrb extends EYBActionWithCallback<ArrayList<AbstractOrb>>
{
    private final ArrayList<AbstractOrb> inducedOrbs = new ArrayList<>();
    private final FuncT0<AbstractOrb> orbConstructor;
    private final int initialOrbSlotCount;
    private AbstractOrb orb;

    public InduceOrb(AbstractOrb orb)
    {
        super(ActionType.SPECIAL, Settings.FAST_MODE ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        this.orb = orb;
        this.orbConstructor = null;
        this.initialOrbSlotCount = player.orbs.size();

        Initialize(1);
    }

    public InduceOrb(FuncT0<AbstractOrb> orbConstructor, int amount)
    {
        super(ActionType.SPECIAL, Settings.FAST_MODE ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        this.orb = null;
        this.orbConstructor = orbConstructor;
        this.initialOrbSlotCount = player.orbs.size();

        Initialize(amount);
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

            if (GameUtilities.IsValidOrb(orb))
            {
                orb.applyFocus();
                GameActions.Top.Callback(new EvokeSpecificOrbAction(orb))
                        .AddCallback(() -> {
                            while (player.orbs.size() > this.initialOrbSlotCount)
                            {
                                player.orbs.remove(player.orbs.size() - 1);
                            }
                        });
                inducedOrbs.add(orb);
            }
            inducedOrbs.add(orb);

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
            Complete(inducedOrbs);
        }
    }
}
