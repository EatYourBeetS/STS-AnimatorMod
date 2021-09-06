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
    protected final ArrayList<AbstractOrb> inducedOrbs = new ArrayList<>();
    protected final FuncT0<AbstractOrb> orbConstructor;
    protected final int initialOrbSlotCount;
    protected boolean shouldTriggerOnEvoke;
    protected AbstractOrb orb;

    public InduceOrb(AbstractOrb orb, boolean shouldTriggerOnEvoke)
    {
        this(orb,null,1,shouldTriggerOnEvoke);
    }

    public InduceOrb(FuncT0<AbstractOrb> orbConstructor, int amount, boolean shouldTriggerOnEvoke)
    {
        this(null,orbConstructor,amount,shouldTriggerOnEvoke);
    }

    public InduceOrb(AbstractOrb orb, FuncT0<AbstractOrb> orbConstructor, int amount, boolean shouldTriggerOnEvoke)
    {
        super(ActionType.SPECIAL, Settings.FAST_MODE ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        this.orb = orb;
        this.orbConstructor = orbConstructor;
        this.initialOrbSlotCount = player.orbs.size();
        this.shouldTriggerOnEvoke = shouldTriggerOnEvoke;

        Initialize(amount);
    }

    public InduceOrb SetShouldTriggerOnEvoke(boolean shouldTriggerOnEvoke)
    {
        this.shouldTriggerOnEvoke = shouldTriggerOnEvoke;

        return this;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (amount > 0)
        {
            if (orbConstructor != null)
            {
                orb = orbConstructor.Invoke();
            }

            if (shouldTriggerOnEvoke) {
                TriggerEffectOnly(orb, amount);
            }
            else {
                TriggerEffectAndEvoke(orb, amount);
            }
        }

        Complete(inducedOrbs);
    }

    protected void TriggerEffectOnly(AbstractOrb orb, int times)
    {
        if (GameUtilities.IsValidOrb(orb))
        {
            for (int i = 0; i < times; i++)
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
        }
    }

    protected void TriggerEffectAndEvoke(AbstractOrb orb, int times)
    {
        if (GameUtilities.IsValidOrb(orb))
        {
            for (int i = 0; i < times; i++)
            {
                orb.applyFocus();
                orb.onEvoke();
                inducedOrbs.add(orb);
            }
        }
    }
}
