package eatyourbeets.actions;

import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.utilities.GenericCondition;

import java.util.ArrayList;

public abstract class EYBActionWithCallbackAdvanced<T> extends EYBActionWithCallback<T>
{
    protected ArrayList<GenericCondition<T>> conditions = new ArrayList<>();

    public EYBActionWithCallbackAdvanced(ActionType type)
    {
        super(type);
    }

    public EYBActionWithCallbackAdvanced(ActionType type, float duration)
    {
        super(type, duration);
    }

    public EYBActionWithCallbackAdvanced<T> AddCondition(Object state, FuncT2<Boolean, Object, T> condition)
    {
        conditions.add(GenericCondition.FromT2(condition, state));

        return this;
    }

    public EYBActionWithCallbackAdvanced<T> AddCondition(FuncT1<Boolean, T> condition)
    {
        conditions.add(GenericCondition.FromT1(condition));

        return this;
    }

    public EYBActionWithCallbackAdvanced<T> AddCondition(FuncT0<Boolean> condition)
    {
        conditions.add(GenericCondition.FromT0(condition));

        return this;
    }

    protected boolean Check(T result)
    {
        for (GenericCondition<T> callback : conditions)
        {
            if (!callback.Check(result))
            {
                return false;
            }
        }

        return true;
    }
}
