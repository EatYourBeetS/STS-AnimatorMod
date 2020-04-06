package eatyourbeets.actions;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.utilities.TargetHelper;

import java.util.List;

public class EYBActionAutoTarget<T> extends EYBActionWithCallback<T>
{
    protected TargetHelper targetHelper;
    protected List<AbstractCreature> targets;

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

    protected List<AbstractCreature> FindTargets()
    {
        targets = targetHelper.GetTargets();

        if (targets.size() > 0)
        {
            target = targets.get(0);
        }

        return targets;
    }
}
