package eatyourbeets.actions;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.utilities.TargetHelper;

import java.util.ArrayList;
import java.util.List;

public class EYBActionAutoTarget<T> extends EYBActionWithCallback<T>
{
    protected final List<AbstractCreature> targets = new ArrayList<>();
    protected TargetHelper targetHelper;

    public EYBActionAutoTarget(ActionType type)
    {
        super(type);
    }

    public EYBActionAutoTarget(ActionType type, float duration)
    {
        super(type, duration);
    }

    protected void Initialize(TargetHelper targetHelper, int amount)
    {
        this.targetHelper = targetHelper;

        Initialize(targetHelper.GetSource(), null, amount);
    }

    protected List<AbstractCreature> FindTargets(boolean reverse)
    {
        targets.clear();

        for (AbstractCreature c : targetHelper.GetTargets(true))
        {
            if (reverse)
            {
                targets.add(0, c);
            }
            else
            {
                targets.add(c);
            }
        }

        if (targets.size() > 0)
        {
            target = targets.get(0);
        }

        return targets;
    }
}
